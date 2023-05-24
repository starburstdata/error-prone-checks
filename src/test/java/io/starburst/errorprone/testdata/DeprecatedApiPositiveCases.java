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
public class DeprecatedApiPositiveCases
{
    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    private final Object instantiation = new DeprecatedClass();

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    private final Object instantiationRecord = new DeprecatedRecord("foo");

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    private DeprecatedClass asField;

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    private DeprecatedEnum asEnumField;

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    private DeprecatedRecord asRecordField;

    private final ClassWithDeprecatedMember classWithMember = new ClassWithDeprecatedMember();

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public void asParameter(DeprecatedClass param)
    {}

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public void asParameter(DeprecatedEnum param)
    {}

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public void asParameter(DeprecatedRecord param)
    {}

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public void asTypeArgument(List<DeprecatedClass> param)
    {}

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public void asTypeArgumentEnum(List<DeprecatedEnum> param)
    {}

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public void asTypeArgumentRecord(List<DeprecatedRecord> param)
    {}

    public void asLocalVariable()
    {
        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        DeprecatedClass var;

        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        DeprecatedEnum varEnum;

        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        DeprecatedRecord varRecord;
    }

    public void methodCall()
    {
        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        classWithMember.deprecated();
    }

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public DeprecatedClass asReturnType()
    {
        return null;
    }

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public DeprecatedEnum asReturnTypeEnum()
    {
        return null;
    }

    // BUG: Diagnostic contains: Do not use @Deprecated APIs.
    public DeprecatedRecord asReturnTypeRecord()
    {
        return null;
    }

    public void referencingStaticMember()
    {
        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        String ignore = DeprecatedClass.STATIC_MEMBER;
    }

    public void referencingStaticMemberAsStaticImport()
    {
        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        String ignore = STATIC_MEMBER;
    }

    public static class AsBaseClass
            // BUG: Diagnostic contains: Do not use @Deprecated APIs.
            extends DeprecatedClass
    {}

    public static class Overrides
            extends ClassWithDeprecatedMember
    {
        @Override
        // BUG: Diagnostic contains: Do not use @Deprecated APIs.
        public void deprecated()
        {}
    }
}
