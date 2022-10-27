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

import io.trino.spi.deprecated.ClassWithDeprecatedMember;
import io.trino.spi.deprecated.DeprecatedClass;

import java.util.List;

public class DeprecatedApiPositiveCases
{
    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    private final Object instantiation = new DeprecatedClass();

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    private DeprecatedClass asField;

    private final ClassWithDeprecatedMember classWithMember = new ClassWithDeprecatedMember();

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public void asParameter(DeprecatedClass param)
    {}

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public void asTypeArgument(List<DeprecatedClass> param)
    {}

    public void asLocalVariable()
    {
        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        DeprecatedClass var;
    }

    public void methodCall()
    {
        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        classWithMember.deprecated();
    }

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public DeprecatedClass asReturnType()
    {
        return null;
    }

    public static class AsBaseClass
            // BUG: Diagnostic contains: Do not use @Deprecated APIs.
            extends DeprecatedClass
    {}

    public static class Overrides
            extends ClassWithDeprecatedMember
    {
        @Override
        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        public void deprecated()
        {}
    }
}
