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
import io.trino.spi.deprecated.NotDeprecatedClass;

import java.util.List;

public class DeprecatedApiNegativeCases
{
    private final Object instantiation = new NotDeprecatedClass();

    private NotDeprecatedClass asField;

    private final ClassWithDeprecatedMember classWithMember = new ClassWithDeprecatedMember();

    public void asParameter(NotDeprecatedClass param)
    {}

    public void asTypeParameter(List<NotDeprecatedClass> param)
    {}

    public void asLocalVariable()
    {
        NotDeprecatedClass var;
    }

    public void methodCall()
    {
        classWithMember.notDeprecated();
    }

    public NotDeprecatedClass asReturnType()
    {
        return null;
    }

    public static class AsBaseClass
            extends NotDeprecatedClass
    {}

    public static class Overrides
            extends ClassWithDeprecatedMember
    {
        @Override
        public void notDeprecated()
        {}
    }
}
