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

import static io.trino.spi.deprecated.DeprecatedClass.STATIC_MEMBER;

@SuppressWarnings({"deprecation", "unused"})
public class DeprecatedApiNegativeCasesIgnored
{
    private final Object instantiation = new DeprecatedClass();

    private DeprecatedClass asField;

    private final ClassWithDeprecatedMember classWithMember = new ClassWithDeprecatedMember();

    public void asParameter(DeprecatedClass param)
    {}

    public void asTypeArgument(List<DeprecatedClass> param)
    {}

    public void asLocalVariable()
    {
        DeprecatedClass var;
    }

    public void methodCall()
    {
        classWithMember.deprecated();
    }

    public DeprecatedClass asReturnType()
    {
        return null;
    }

    public void referencingStaticMember()
    {
        String ignore = DeprecatedClass.STATIC_MEMBER;
    }

    public void referencingStaticMemberAsStaticImport()
    {
        String ignore = STATIC_MEMBER;
    }

    public static class AsBaseClass
            extends DeprecatedClass
    {}

    public static class Overrides
            extends ClassWithDeprecatedMember
    {
        @Override
        public void deprecated()
        {}
    }
}
