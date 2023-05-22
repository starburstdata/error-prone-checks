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
public class TrinoExperimentalSpiNegativeCasesSuppressed
{
    @SuppressWarnings("TrinoExperimentalSpi")
    private final Object instantiation = new ExperimentalClass();

    @SuppressWarnings("TrinoExperimentalSpi")
    private ExperimentalClass asField;

    private final ClassWithExperimentalMember classWithMember = new ClassWithExperimentalMember();

    @SuppressWarnings("TrinoExperimentalSpi")
    public void asParameter(ExperimentalClass param)
    {}

    @SuppressWarnings("TrinoExperimentalSpi")
    public void asTypeArgument(List<ExperimentalClass> param)
    {}

    public void asLocalVariable()
    {
        @SuppressWarnings("TrinoExperimentalSpi")
        ExperimentalClass var;
    }

    @SuppressWarnings("TrinoExperimentalSpi")
    public void methodCall()
    {
        classWithMember.experimental();
    }

    @SuppressWarnings("TrinoExperimentalSpi")
    public ExperimentalClass asReturnType()
    {
        return null;
    }

    public void referencingStaticMember()
    {
        @SuppressWarnings("TrinoExperimentalSpi")
        String ignore = ExperimentalClass.STATIC_MEMBER;
    }

    public void referencingStaticMemberAsStaticImport()
    {
        @SuppressWarnings("TrinoExperimentalSpi")
        String ignore = STATIC_MEMBER;
    }

    @SuppressWarnings("TrinoExperimentalSpi")
    public static class AsBaseClass
            extends ExperimentalClass
    {}

    public static class Overrides
            extends ClassWithExperimentalMember
    {
        @SuppressWarnings("TrinoExperimentalSpi")
        @Override
        public void experimental()
        {}
    }
}
