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

public class DeprecatedApiNegativeCasesSuppressed
{
    @SuppressWarnings({"deprecation", "DeprecatedApi"})
    private final Object instantiation = new DeprecatedClass();

    @SuppressWarnings({"deprecation", "DeprecatedApi"})
    private DeprecatedClass asField;

    private final ClassWithDeprecatedMember classWithMember = new ClassWithDeprecatedMember();

    @SuppressWarnings({"deprecation", "DeprecatedApi"})
    public void asParameter(DeprecatedClass param)
    {}

    @SuppressWarnings({"deprecation", "DeprecatedApi"})
    public void asTypeArgument(List<DeprecatedClass> param)
    {}

    public void asLocalVariable()
    {
        @SuppressWarnings({"deprecation", "DeprecatedApi"})
        DeprecatedClass var;
    }

    @SuppressWarnings({"deprecation", "DeprecatedApi"})
    public void methodCall()
    {
        classWithMember.deprecated();
    }

    @SuppressWarnings({"deprecation", "DeprecatedApi"})
    public DeprecatedClass asReturnType()
    {
        return null;
    }

    @SuppressWarnings({"deprecation", "DeprecatedApi"})
    public static class AsBaseClass
            extends DeprecatedClass
    {}
}
