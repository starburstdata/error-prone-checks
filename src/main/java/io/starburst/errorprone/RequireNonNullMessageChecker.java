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
import com.google.errorprone.bugpatterns.BugChecker.MethodInvocationTreeMatcher;
import com.google.errorprone.fixes.SuggestedFix;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.matchers.Matcher;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.google.errorprone.matchers.Description.NO_MATCH;
import static com.google.errorprone.matchers.method.MethodMatchers.staticMethod;
import static com.google.errorprone.util.ASTHelpers.getSymbol;
import static com.sun.source.tree.Tree.Kind.IDENTIFIER;
import static com.sun.source.tree.Tree.Kind.STRING_LITERAL;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@AutoService(BugChecker.class)
@BugPattern(
        name = "RequireNonNullMessage",
        summary = "An error message provided to Objects#requireNonNull is incorrect",
        explanation = """
                The error message passed as the second argument to Objects#requireNonNull \
                should make it immediately obvious which value is null; the check will trigger when \
                an identifier is passes as the first argument to Objects#requireNonNull, \
                but the error message passes as the second argument does not mention its name.""",
        linkType = BugPattern.LinkType.NONE,
        severity = BugPattern.SeverityLevel.WARNING)
public class RequireNonNullMessageChecker
        extends BugChecker
        implements MethodInvocationTreeMatcher
{
    private static final Pattern MESSAGE_PATTERN = Pattern.compile(" (?:(?:is|are|was|were) (?:null|empty|missing|none)|(?:is|are) required|(?:must not|cannot|can't) be (?:null|empty))");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^\\p{javaUnicodeIdentifierStart}\\p{javaUnicodeIdentifierPart}*\\b");

    private static final Matcher<ExpressionTree> requireNonNull = staticMethod()
            .onClass(Objects.class.getName())
            .named("requireNonNull");

    @Override
    public Description matchMethodInvocation(MethodInvocationTree tree, VisitorState state)
    {
        if (!requireNonNull.matches(tree, state)) {
            return NO_MATCH;
        }
        List<? extends ExpressionTree> arguments = tree.getArguments();
        if (arguments.isEmpty()) {
            // weird
            return NO_MATCH;
        }

        // no error message at all:
        if (arguments.size() < 2) {
            // this is considered fine: it's probably just a sanity check
            // (we could enable it at some point, but let's just focus on typos for now)
            return NO_MATCH;
        }

        // the first argument: identifier or an expression?
        ExpressionTree objectArgument = arguments.get(0);
        if (objectArgument.getKind() != IDENTIFIER) {
            // we don't want to deal with complex expressions; we only want to detect typos in simple cases
            return NO_MATCH;
        }
        String objectArgumentIdentifier = requireNonNull(state.getSourceForNode(objectArgument));

        // inspect the message
        ExpressionTree messageArgument = arguments.get(1);
        if (messageArgument.getKind() != STRING_LITERAL) {
            // something else than a literal: ignore, since we can't inspect it
            // TODO: suggest using message supplier
            return NO_MATCH;
        }
        String messageLiteral = (String) ((LiteralTree) messageArgument).getValue();

        // we will first see if we can recognize the error message as "X is null" kind of thing, then proceed accordingly
        // note: there's no end-of-string anchor in the pattern, so we allow extra information at the end of the message
        // (and it starts with a space instead of the start-of-string anchor!)
        java.util.regex.Matcher patternMatch = MESSAGE_PATTERN.matcher(messageLiteral);
        if (!patternMatch.find()) {
            // the message does not fit the pattern - must be some free-form text which we won't try to interpret
            // checks in constructors are a special case, though, so require that the message is equal to the identifier's name
            if (isWithinConstructor(state) && !messageLiteral.equals(objectArgumentIdentifier)) {
                return describeMatch(tree, (LiteralTree) messageArgument, objectArgumentIdentifier, " is null", "");
            }

            return NO_MATCH;
        }

        // now that we know it's "X is null" kind of thing, let's interpret the "X" part
        // note: the pattern ends with a word-break, so we won't get a positive match
        // if only part of the beginning is recognizable as an identifier
        java.util.regex.Matcher identifierMatch = IDENTIFIER_PATTERN.matcher(messageLiteral);
        if (!identifierMatch.find()) {
            // the thing before the pattern is not an identifier at all: ignore, but not in constructors
            if (isWithinConstructor(state)) {
                return describeMatch(tree, (LiteralTree) messageArgument, objectArgumentIdentifier, patternMatch.group(), messageLiteral.substring(patternMatch.end()));
            }

            return NO_MATCH;
        }

        if (identifierMatch.end() != patternMatch.start()) {
            // the thing before the pattern is an identifier, but with some extra bits
            // after the word-break: ignore, but not in constructors
            // (e.g. 'requireNonNull(parameter, "parameter somehow is null")' is fine)
            if (isWithinConstructor(state) && !identifierMatch.group().equals(objectArgumentIdentifier)) {
                return describeMatch(tree, (LiteralTree) messageArgument, objectArgumentIdentifier, patternMatch.group(), messageLiteral.substring(patternMatch.end()));
            }

            return NO_MATCH;
        }

        if (identifierMatch.group().equals(objectArgumentIdentifier)) {
            // not a typo: no error
            return NO_MATCH;
        }

        // looks like something we're looking for: success!
        return describeMatch(tree, (LiteralTree) messageArgument, objectArgumentIdentifier, patternMatch.group(), messageLiteral.substring(patternMatch.end()));
    }

    private Description describeMatch(MethodInvocationTree tree, LiteralTree messageArgument, String objectArgumentIdentifier, String pattern, String suffix)
    {
        return describeMatch(
                tree,
                SuggestedFix.replace(
                        messageArgument,
                        format("\"%s%s%s\"", objectArgumentIdentifier, pattern, suffix)));
    }

    private boolean isWithinConstructor(VisitorState state)
    {
        MethodTree enclosingMethodTree = state.findEnclosing(MethodTree.class);
        return enclosingMethodTree != null && getSymbol(enclosingMethodTree).isConstructor();
    }
}
