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

import io.trino.spi.experimental.ClassWithExperimentalMember;
import io.trino.spi.experimental.ExperimentalClass;

import java.util.List;

import static io.trino.spi.experimental.ExperimentalClass.STATIC_MEMBER;

@SuppressWarnings("unused")
public class TrinoExperimentalSpiNegativeCasesIgnored
{
    private final Object instantiation = new ExperimentalClass();

    private ExperimentalClass asField;

    private final ClassWithExperimentalMember classWithMember = new ClassWithExperimentalMember();

    public void asParameter(ExperimentalClass param)
    {}

    public void asTypeArgument(List<ExperimentalClass> param)
    {}

    public void asLocalVariable()
    {
        ExperimentalClass var;
    }

    public void methodCall()
    {
        classWithMember.experimental();
    }

    public ExperimentalClass asReturnType()
    {
        return null;
    }

    public void referencingStaticMember()
    {
        String ignore = ExperimentalClass.STATIC_MEMBER;
    }

    public void referencingStaticMemberAsStaticImport()
    {
        String ignore = STATIC_MEMBER;
    }

    public static class AsBaseClass
            extends ExperimentalClass
    {}

    public static class Overrides
            extends ClassWithExperimentalMember
    {
        @Override
        public void experimental()
        {}
    }
}
