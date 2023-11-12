/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */
package io.starburst.errorprone.testdata;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class RequireNonNullMessageNegativeCases
{
    private Object placeholder;

    @SuppressWarnings("RegexpSingleline") // "Objects.requireNonNull should only be used with static imports" - we do it on purpose
    public RequireNonNullMessageNegativeCases(Object fullPatternInConstructorWithoutStaticImport)
    {
        this.placeholder = Objects.requireNonNull(fullPatternInConstructorWithoutStaticImport, "fullPatternInConstructorWithoutStaticImport is null");
    }

    public RequireNonNullMessageNegativeCases(Byte bareIdentifierInConstructor)
    {
        this.placeholder = requireNonNull(bareIdentifierInConstructor, "bareIdentifierInConstructor");
    }

    public RequireNonNullMessageNegativeCases(Byte fullPatternInConstructor, int dummy)
    {
        this.placeholder = requireNonNull(fullPatternInConstructor, "fullPatternInConstructor is null");
    }

    public RequireNonNullMessageNegativeCases(Byte fullPatternWithInfixInConstructor, long dummy)
    {
        this.placeholder = requireNonNull(fullPatternWithInfixInConstructor, "fullPatternWithInfixInConstructor clearly is null");
    }

    public RequireNonNullMessageNegativeCases(Boolean fullPatternNestedInConstructor)
    {
        if (true == false) {
            for (String s : List.of("a", "b", "c")) {
                this.placeholder = requireNonNull(fullPatternNestedInConstructor, "fullPatternNestedInConstructor is null");
            }
        }
    }

    public RequireNonNullMessageNegativeCases(Integer messageMissingInConstructor)
    {
        // this is considered fine: just a quick sanity check
        this.placeholder = requireNonNull(messageMissingInConstructor);
    }

    public RequireNonNullMessageNegativeCases(String complexExpressionInConstructor)
    {
        // we only want to find typos and other simple cases, so ignore complex expressions
        this.placeholder = requireNonNull(getClass(), "I don't like this parameter");
    }

    @SuppressWarnings("RegexpSingleline") // "Objects.requireNonNull should only be used with static imports" - we do it on purpose
    public void fullPatternInMethodWithoutStaticImport(String parameter)
    {
        this.placeholder = Objects.requireNonNull(parameter, "parameter is null");
    }

    public void bareIdentifierInMethod(Object parameter)
    {
        this.placeholder = requireNonNull(parameter, "parameter");
    }

    public void fullPatternInMethod(Object parameter)
    {
        this.placeholder = requireNonNull(parameter, "parameter is null");
    }

    public void fullPatternWithInfixInMethod(Object parameter)
    {
        this.placeholder = requireNonNull(parameter, "parameter clearly is null");
    }

    public void fullPatternNestedInMethod(Object parameter)
    {
        if (true == false) {
            for (String s : List.of("a", "b", "c")) {
                this.placeholder = requireNonNull(parameter, "parameter is null");
            }
        }
    }

    public void messageMissingInMethod(String parameter)
    {
        // this is considered fine: just check the stack trace
        this.placeholder = requireNonNull(parameter);
    }

    public void messageAlmostMalformedInMethod(String parameter)
    {
        // we're only flagging cases where the entire thing before the pattern looks like an identifier
        this.placeholder = requireNonNull(parameter, "my argument is null");
    }

    public void messageNotFittingPatternInMethod(String parameter)
    {
        // someone wanted to provide a more descriptive message, and this is fine
        this.placeholder = requireNonNull(parameter, "I don't like this parameter");
    }

    public void complexExpressionInMethod(String parameter)
    {
        // we only want to find typos and other simple cases, so ignore complex expressions
        this.placeholder = requireNonNull(parameter.getClass(), "I don't like this parameter");
    }
}
