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
import io.trino.spi.experimental.ExperimentalEnum;
import io.trino.spi.experimental.ExperimentalRecord;

import java.util.List;

import static io.trino.spi.experimental.ExperimentalClass.STATIC_MEMBER;

@SuppressWarnings("unused")
public class TrinoExperimentalSpiNegativeCasesSuppressed
{
    @SuppressWarnings("TrinoExperimentalSpi")
    private final Object instantiation = new ExperimentalClass();

    @SuppressWarnings("TrinoExperimentalSpi")
    private final Object instantiationRecord = new ExperimentalRecord("foo");

    @SuppressWarnings("TrinoExperimentalSpi")
    private ExperimentalClass asField;

    @SuppressWarnings("TrinoExperimentalSpi")
    private ExperimentalEnum asEnumField;

    @SuppressWarnings("TrinoExperimentalSpi")
    private ExperimentalRecord asRecordField;

    private final ClassWithExperimentalMember classWithMember = new ClassWithExperimentalMember();

    @SuppressWarnings("TrinoExperimentalSpi")
    public void asParameter(ExperimentalClass param)
    {}

    @SuppressWarnings("TrinoExperimentalSpi")
    public void asParameter(ExperimentalEnum param)
    {}

    @SuppressWarnings("TrinoExperimentalSpi")
    public void asParameter(ExperimentalRecord param)
    {}

    @SuppressWarnings("TrinoExperimentalSpi")
    public void asTypeArgument(List<ExperimentalClass> param)
    {}

    @SuppressWarnings("TrinoExperimentalSpi")
    public void asTypeArgumentEnum(List<ExperimentalEnum> param)
    {}

    @SuppressWarnings("TrinoExperimentalSpi")
    public void asTypeArgumentRecord(List<ExperimentalRecord> param)
    {}

    public void asLocalVariable()
    {
        @SuppressWarnings("TrinoExperimentalSpi")
        ExperimentalClass var;

        @SuppressWarnings("TrinoExperimentalSpi")
        ExperimentalEnum varEnum;

        @SuppressWarnings("TrinoExperimentalSpi")
        ExperimentalRecord varRecord;
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

    @SuppressWarnings("TrinoExperimentalSpi")
    public ExperimentalEnum asReturnTypeEnum()
    {
        return null;
    }

    @SuppressWarnings("TrinoExperimentalSpi")
    public ExperimentalRecord asReturnTypeRecord()
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
