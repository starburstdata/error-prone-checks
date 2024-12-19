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

public class RequireNonNullMessagePositiveCases
{
    private Object placeholder;

    @SuppressWarnings("RegexpSingleline") // "Objects.requireNonNull should only be used with static imports" - we do it on purpose
    public RequireNonNullMessagePositiveCases(Integer messageMalformedInConstructorWithStaticImport)
    {
        // BUG: Diagnostic contains: RequireNonNullMessage
        // Did you mean 'this.placeholder = Objects.requireNonNull(messageMalformedInConstructorWithStaticImport, "messageMalformedInConstructorWithStaticImport is null");'?
        this.placeholder = Objects.requireNonNull(messageMalformedInConstructorWithStaticImport, "messageMissingInConstructor is null");
    }

    public RequireNonNullMessagePositiveCases(Long messageMalformedInConstructor)
    {
        // BUG: Diagnostic contains: RequireNonNullMessage
        // Did you mean 'this.placeholder = requireNonNull(messageMalformedInConstructor, "messageMalformedInConstructor is null");'?
        this.placeholder = requireNonNull(messageMalformedInConstructor, "messageMissingInConstructor is null");
    }

    public RequireNonNullMessagePositiveCases(Character messageMalformedNestedInConstructor)
    {
        if (true == false) {
            for (String s : List.of("a", "b", "c")) {
                // BUG: Diagnostic contains: RequireNonNullMessage
                // Did you mean 'this.placeholder = requireNonNull(messageMalformedNestedInConstructor, "messageMalformedNestedInConstructor is null");'?
                this.placeholder = requireNonNull(messageMalformedNestedInConstructor, "messageMissingInConstructor is null");
            }
        }
    }

    public RequireNonNullMessagePositiveCases(Float messageAlmostMalformedInConstructor)
    {
        // looks like an identifier, but it's not our identifier
        // BUG: Diagnostic contains: RequireNonNullMessage
        // Did you mean 'this.placeholder = requireNonNull(messageAlmostMalformedInConstructor, "messageAlmostMalformedInConstructor is null");'?
        this.placeholder = requireNonNull(messageAlmostMalformedInConstructor, "my argument is null");
    }

    public RequireNonNullMessagePositiveCases(Double messageNotFittingPatternInConstructor)
    {
        // BUG: Diagnostic contains: RequireNonNullMessage
        // Did you mean 'this.placeholder = requireNonNull(messageNotFittingPatternInConstructor, "messageNotFittingPatternInConstructor is null");'?
        this.placeholder = requireNonNull(messageNotFittingPatternInConstructor, "I don't like this parameter");
    }

    public void messageMalformedInMethod(String parameter)
    {
        // BUG: Diagnostic contains: RequireNonNullMessage
        // Did you mean 'this.placeholder = requireNonNull(parameter, "parameter is null");'?
        this.placeholder = requireNonNull(parameter, "argument is null");
    }

    public void messageMalformedNestedInMethod(String parameter)
    {
        if (true == false) {
            for (String s : List.of("a", "b", "c")) {
                // BUG: Diagnostic contains: RequireNonNullMessage
                // Did you mean 'this.placeholder = requireNonNull(parameter, "parameter is null");'?
                this.placeholder = requireNonNull(parameter, "argument is null");
            }
        }
    }
}
