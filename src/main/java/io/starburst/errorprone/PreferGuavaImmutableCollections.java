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

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.fixes.SuggestedFix;
import com.google.errorprone.fixes.SuggestedFixes;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;

import java.util.Optional;

import static com.google.errorprone.BugPattern.SeverityLevel.WARNING;
import static com.google.errorprone.matchers.Description.NO_MATCH;

@AutoService(BugChecker.class)
@BugPattern(
        summary = "Prefer Guava immutable collections to avoid copies between JDK and Guava collections.",
        linkType = BugPattern.LinkType.NONE,
        severity = WARNING)
public final class PreferGuavaImmutableCollections
        extends BugChecker
        implements BugChecker.MethodInvocationTreeMatcher, BugChecker.MemberReferenceTreeMatcher
{
    @Override
    public Description matchMethodInvocation(MethodInvocationTree tree, VisitorState state)
    {
        MethodSymbol symbol = ASTHelpers.getSymbol(tree);
        if (symbol == null) {
            return NO_MATCH;
        }
        Optional<Replacement> replacement = replacement(symbol);
        if (replacement.isEmpty()) {
            return NO_MATCH;
        }

        Description.Builder description = buildDescription(tree);
        Replacement target = replacement.orElseThrow();
        if (!target.hasFix() || isInferredVariableInitializer(state) || (target.method().equals("toImmutableList") && !isStreamCollectArgument(tree, state))) {
            return description.build();
        }

        String method = target.method();
        if (symbol.isVarArgs() && tree.getArguments().size() == 1) {
            Type argumentType = ASTHelpers.getType(tree.getArguments().getFirst());
            if (argumentType == null) {
                return description.build();
            }
            if (argumentType.hasTag(TypeTag.ARRAY)) {
                method = "copyOf";
            }
        }
        SuggestedFix.Builder fix = SuggestedFix.builder();
        String type = SuggestedFixes.qualifyType(state, fix, target.type());
        if (tree.getMethodSelect() instanceof MemberSelectTree select) {
            String source = state.getSourceForNode(select);
            if (source == null || !source.endsWith(symbol.getSimpleName().toString())) {
                return description.build();
            }
            fix.replace(select.getExpression(), type);
            int end = state.getEndPosition(select);
            fix.replace(end - symbol.getSimpleName().length(), end, method);
        }
        else {
            fix.replace(tree.getMethodSelect(), type + "." + method);
        }
        return description.addFix(fix.build()).build();
    }

    @Override
    public Description matchMemberReference(MemberReferenceTree tree, VisitorState state)
    {
        MethodSymbol symbol = (MethodSymbol) ASTHelpers.getSymbol(tree);
        Optional<Replacement> replacement = replacement(symbol);
        if (replacement.isEmpty()) {
            return NO_MATCH;
        }

        Description.Builder description = buildDescription(tree);
        Replacement target = replacement.orElseThrow();
        if (!target.hasFix() || !target.method().equals("copyOf") || isInferredVariableInitializer(state)) {
            return description.build();
        }

        SuggestedFix.Builder fix = SuggestedFix.builder();
        String type = SuggestedFixes.qualifyType(state, fix, target.type());
        fix.replace(tree.getQualifierExpression(), type);
        return description.addFix(fix.build()).build();
    }

    private static boolean isInferredVariableInitializer(VisitorState state)
    {
        for (TreePath path = state.getPath(); path != null; path = path.getParentPath()) {
            if (path.getLeaf() instanceof VariableTree variable) {
                return ASTHelpers.hasImplicitType(variable, state);
            }
            if (path.getLeaf() instanceof MethodTree) {
                return false;
            }
        }
        return false;
    }

    private static boolean isStreamCollectArgument(MethodInvocationTree tree, VisitorState state)
    {
        if (!(state.getPath().getParentPath().getLeaf() instanceof MethodInvocationTree parent) || !parent.getArguments().contains(tree)) {
            return false;
        }
        MethodSymbol symbol = ASTHelpers.getSymbol(parent);
        return symbol != null && symbol.owner.getQualifiedName().contentEquals("java.util.stream.Stream") && symbol.getSimpleName().contentEquals("collect");
    }

    private static Optional<Replacement> replacement(MethodSymbol symbol)
    {
        if (symbol == null) {
            return Optional.empty();
        }
        String owner = symbol.owner.getQualifiedName().toString();
        String method = symbol.getSimpleName().toString();
        return switch (owner) {
            case "java.util.List" -> switch (method) {
                case "of", "copyOf" -> Optional.of(new Replacement("com.google.common.collect.ImmutableList", method, true));
                default -> Optional.empty();
            };
            case "java.util.Set" -> switch (method) {
                case "of", "copyOf" -> Optional.of(new Replacement("com.google.common.collect.ImmutableSet", method, false));
                default -> Optional.empty();
            };
            case "java.util.Map" -> switch (method) {
                case "of", "ofEntries", "copyOf" -> Optional.of(new Replacement("com.google.common.collect.ImmutableMap", method, false));
                default -> Optional.empty();
            };
            case "java.util.stream.Collectors" -> switch (method) {
                case "toUnmodifiableList" -> Optional.of(new Replacement("com.google.common.collect.ImmutableList", "toImmutableList", true));
                case "toUnmodifiableSet" -> Optional.of(new Replacement("com.google.common.collect.ImmutableSet", "toImmutableSet", false));
                case "toUnmodifiableMap" -> Optional.of(new Replacement("com.google.common.collect.ImmutableMap", "toImmutableMap", false));
                default -> Optional.empty();
            };
            default -> Optional.empty();
        };
    }

    private record Replacement(String type, String method, boolean hasFix) {}
}
