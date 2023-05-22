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
public class TrinoExperimentalSpiPositiveCases
{
    // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
    private final Object instantiation = new ExperimentalClass();

    // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
    private ExperimentalClass asField;

    private final ClassWithExperimentalMember classWithMember = new ClassWithExperimentalMember();

    // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
    public void asParameter(ExperimentalClass param)
    {}

    // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
    public void asTypeArgument(List<ExperimentalClass> param)
    {}

    public void asLocalVariable()
    {
        // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
        ExperimentalClass var;
    }

    public void methodCall()
    {
        // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
        classWithMember.experimental();
    }

    // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
    public ExperimentalClass asReturnType()
    {
        return null;
    }

    public void referencingStaticMember()
    {
        // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
        String ignore = ExperimentalClass.STATIC_MEMBER;
    }

    public void referencingStaticMemberAsStaticImport()
    {
        // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
        String ignore = STATIC_MEMBER;
    }

    public static class AsBaseClass
            // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
            extends ExperimentalClass
    {}

    public static class Overrides
            extends ClassWithExperimentalMember
    {
        @Override
        // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs.
        public void experimental()
        {}
    }
}
