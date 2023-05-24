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

@SuppressWarnings({"deprecation", "unused"})
public class DeprecatedApiNegativeCasesSuppressed
{
    @SuppressWarnings("DeprecatedApi")
    private final Object instantiation = new DeprecatedClass();

    @SuppressWarnings("DeprecatedApi")
    private DeprecatedClass asField;

    private final ClassWithDeprecatedMember classWithMember = new ClassWithDeprecatedMember();

    @SuppressWarnings("DeprecatedApi")
    public void asParameter(DeprecatedClass param)
    {}

    @SuppressWarnings("DeprecatedApi")
    public void asTypeArgument(List<DeprecatedClass> param)
    {}

    public void asLocalVariable()
    {
        @SuppressWarnings("DeprecatedApi")
        DeprecatedClass var;
    }

    @SuppressWarnings("DeprecatedApi")
    public void methodCall()
    {
        classWithMember.deprecated();
    }

    @SuppressWarnings("DeprecatedApi")
    public DeprecatedClass asReturnType()
    {
        return null;
    }

    @SuppressWarnings("DeprecatedApi")
    public static class AsBaseClass
            extends DeprecatedClass
    {}

    public static class Overrides
            extends ClassWithDeprecatedMember
    {
        @SuppressWarnings("DeprecatedApi")
        @Override
        public void deprecated()
        {}
    }
}
