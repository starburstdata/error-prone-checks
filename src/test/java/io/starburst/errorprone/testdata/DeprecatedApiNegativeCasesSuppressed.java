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
import io.trino.spi.deprecated.DeprecatedEnum;
import io.trino.spi.deprecated.DeprecatedRecord;

import java.util.List;

import static io.trino.spi.deprecated.DeprecatedClass.STATIC_MEMBER;

@SuppressWarnings({"deprecation", "unused"})
public class DeprecatedApiNegativeCasesSuppressed
{
    @SuppressWarnings("DeprecatedApi")
    private final Object instantiation = new DeprecatedClass();

    @SuppressWarnings("DeprecatedApi")
    private final Object instantiationRecord = new DeprecatedRecord("foo");

    @SuppressWarnings("DeprecatedApi")
    private DeprecatedClass asField;

    @SuppressWarnings("DeprecatedApi")
    private DeprecatedEnum asEnumField;

    @SuppressWarnings("DeprecatedApi")
    private DeprecatedRecord asRecordField;

    private final ClassWithDeprecatedMember classWithMember = new ClassWithDeprecatedMember();

    @SuppressWarnings("DeprecatedApi")
    public void asParameter(DeprecatedClass param)
    {}

    @SuppressWarnings("DeprecatedApi")
    public void asParameter(DeprecatedEnum param)
    {}

    @SuppressWarnings("DeprecatedApi")
    public void asParameter(DeprecatedRecord param)
    {}

    @SuppressWarnings("DeprecatedApi")
    public void asTypeArgument(List<DeprecatedClass> param)
    {}

    @SuppressWarnings("DeprecatedApi")
    public void asTypeArgumentEnum(List<DeprecatedEnum> param)
    {}

    @SuppressWarnings("DeprecatedApi")
    public void asTypeArgumentRecord(List<DeprecatedRecord> param)
    {}

    public void asLocalVariable()
    {
        @SuppressWarnings("DeprecatedApi")
        DeprecatedClass var;

        @SuppressWarnings("DeprecatedApi")
        DeprecatedEnum varEnum;

        @SuppressWarnings("DeprecatedApi")
        DeprecatedRecord varRecord;
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

    public void referencingStaticMember()
    {
        @SuppressWarnings("DeprecatedApi")
        String ignore = DeprecatedClass.STATIC_MEMBER;
    }

    public void referencingStaticMemberAsStaticImport()
    {
        @SuppressWarnings("DeprecatedApi")
        String ignore = STATIC_MEMBER;
    }

    @SuppressWarnings("DeprecatedApi")
    public DeprecatedEnum asReturnTypeEnum()
    {
        return null;
    }

    @SuppressWarnings("DeprecatedApi")
    public DeprecatedEnum asReturnTypeRecord()
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
