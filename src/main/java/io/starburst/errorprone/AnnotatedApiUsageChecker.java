/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */

package io.starburst.errorprone;

import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.IdentifierTreeMatcher;
import com.google.errorprone.bugpatterns.BugChecker.MemberReferenceTreeMatcher;
import com.google.errorprone.bugpatterns.BugChecker.MemberSelectTreeMatcher;
import com.google.errorprone.bugpatterns.BugChecker.MethodTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;

import java.util.Optional;
import java.util.Set;

import static com.google.errorprone.matchers.Description.NO_MATCH;
import static javax.lang.model.element.ElementKind.ANNOTATION_TYPE;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.ElementKind.METHOD;

/**
 * Abstract check for usages of APIs that are annotated with a specific annotation.
 *
 * <p>Notice: this checker won't produce a match for {@code super()} calls from classes subclassing a non-annotated class
 * that has an annotated no-args constructor.</p>
 *
 * <p>Adapted from <a href="https://github.com/google/guava-beta-checker/blob/9b26aa980be7f70631921fd6695013547728eb1e/src/main/java/com/google/common/annotations/checkers/AnnotatedApiUsageChecker.java"
 * >AnnotatedApiUsageChecker</a></p>
 */
public abstract class AnnotatedApiUsageChecker
        extends BugChecker
        implements IdentifierTreeMatcher, MemberReferenceTreeMatcher, MemberSelectTreeMatcher, MethodTreeMatcher
{
    /**
     * Kinds of elements that should be considered annotated if the element's owner (i.e. the class
     * it's declared in) is annotated. This is used to prevent things like type parameters that happen
     * to be declared in an annotated class from being flagged.
     */
    private static final Set<ElementKind> INHERITS_ANNOTATION_FROM_OWNER = Set.of(
            FIELD, METHOD, CONSTRUCTOR, ENUM_CONSTANT, CLASS, INTERFACE, ENUM, ANNOTATION_TYPE);

    private final String annotationType;

    private final Set<String> basePackages;
    private final Set<String> ignoredPackages;
    private final Set<String> ignoredTypes;

    protected AnnotatedApiUsageChecker(String annotationType, Set<String> basePackages, Set<String> ignoredPackages, Set<String> ignoredTypes)
    {
        this.annotationType = annotationType;
        this.basePackages = Set.copyOf(basePackages);
        this.ignoredPackages = Set.copyOf(ignoredPackages);
        this.ignoredTypes = Set.copyOf(ignoredTypes);
    }

    protected abstract String messageForMatchingBasePackage(String packageName);

    @Override
    public final Description matchMemberSelect(MemberSelectTree tree, VisitorState state)
    {
        if (ASTHelpers.findEnclosingNode(state.getPath(), ImportTree.class) != null) {
            return NO_MATCH;
        }
        return matchTree(tree);
    }

    @Override
    public final Description matchIdentifier(IdentifierTree tree, VisitorState state)
    {
        return isSuperCall(tree) ? NO_MATCH : matchTree(tree);
    }

    @Override
    public final Description matchMemberReference(MemberReferenceTree tree, VisitorState state)
    {
        return matchTree(tree);
    }

    @Override
    public Description matchMethod(MethodTree methodTree, VisitorState visitorState)
    {
        Symbol.MethodSymbol methodSym = ASTHelpers.getSymbol(methodTree);
        if (methodSym == null) {
            return NO_MATCH;
        }
        for (Symbol.MethodSymbol method : ASTHelpers.findSuperMethods(methodSym, visitorState.getTypes())) {
            if (ASTHelpers.hasAnnotation(method, annotationType, visitorState)) {
                return matchSymbol(methodTree, method.baseSymbol());
            }
        }
        return NO_MATCH;
    }

    private Description matchTree(Tree tree)
    {
        return matchSymbol(tree, ASTHelpers.getSymbol(tree));
    }

    private Description matchSymbol(Tree tree, Symbol symbol)
    {
        if (symbol == null || !isAnnotatedApi(symbol)) {
            return NO_MATCH;
        }
        if (ignoredPackages.contains(symbol.packge().getQualifiedName().toString())) {
            return NO_MATCH;
        }
        if (basePackages.isEmpty()) {
            return describeMatch(tree);
        }
        return findMatchingBasePackage(symbol)
                .map(packageName -> buildDescription(tree)
                        .setMessage(messageForMatchingBasePackage(packageName))
                        .build())
                .orElse(NO_MATCH);
    }

    private static boolean isSuperCall(IdentifierTree tree)
    {
        return tree.getName().contentEquals("super");
    }

    private Optional<String> findMatchingBasePackage(Symbol symbol)
    {
        String packageName = symbol.packge().getQualifiedName().toString();
        if (basePackages.contains(packageName)) {
            return Optional.of(packageName);
        }
        for (String basePackage : basePackages) {
            if (packageName.startsWith(basePackage + ".")) {
                return Optional.of(basePackage);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns true if the given symbol is annotated with the annotation or if it's a member of a type
     * annotated with the annotation.
     */
    private boolean isAnnotatedApi(Symbol symbol)
    {
        Name name = symbol.getQualifiedName();
        if (name != null && ignoredTypes.contains(name.toString())) {
            return false;
        }

        for (AnnotationMirror annotation : symbol.getAnnotationMirrors()) {
            if (annotation.getAnnotationType().toString().equals(annotationType)) {
                return true;
            }
        }

        return isMemberOfAnnotatedApi(symbol);
    }

    private boolean isMemberOfAnnotatedApi(Symbol symbol)
    {
        return symbol != null
                && INHERITS_ANNOTATION_FROM_OWNER.contains(symbol.getKind())
                && isAnnotatedApi(symbol.owner);
    }
}
