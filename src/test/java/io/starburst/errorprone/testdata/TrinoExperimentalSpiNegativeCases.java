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
import io.trino.spi.experimental.NotExperimentalClass;

import java.util.List;

@SuppressWarnings("unused")
public class TrinoExperimentalSpiNegativeCases
{
    private final Object instantiation = new NotExperimentalClass();

    private NotExperimentalClass asField;

    private final ClassWithExperimentalMember classWithMember = new ClassWithExperimentalMember();

    public void asParameter(NotExperimentalClass param)
    {}

    public void asTypeParameter(List<NotExperimentalClass> param)
    {}

    public void asLocalVariable()
    {
        NotExperimentalClass var;
    }

    public void methodCall()
    {
        classWithMember.notExperimental();
    }

    public NotExperimentalClass asReturnType()
    {
        return null;
    }

    public static class AsBaseClass
            extends NotExperimentalClass
    {}

    public static class Overrides
            extends ClassWithExperimentalMember
    {
        @SuppressWarnings("RedundantMethodOverride")
        @Override
        public void notExperimental()
        {}
    }
}
