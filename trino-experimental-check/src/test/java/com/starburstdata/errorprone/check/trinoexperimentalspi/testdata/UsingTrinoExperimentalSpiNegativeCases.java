/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */

package com.starburstdata.errorprone.check.trinoexperimentalspi.testdata;

import io.trino.spi.trinoexperimentalspi.ClassWithAnnotatedMember;
import io.trino.spi.trinoexperimentalspi.NotAnnotatedClass;

import java.util.List;

public class UsingTrinoExperimentalSpiNegativeCases
{
    private final Object instantiationOfNotAnnotatedClas = new NotAnnotatedClass();

    private NotAnnotatedClass notAnnotatedClassAsField;

    private final ClassWithAnnotatedMember classWithAnnotatedMember = new ClassWithAnnotatedMember();

    private NotAnnotatedClass notAnnotatedApiAsInstanceVariable;

    public void notAnnotatedApiAsParameter(NotAnnotatedClass param)
    {}

    public void notAnnotatedApiAsTypeParameter(List<NotAnnotatedClass> param)
    {}

    public void notAnnotatedApiAsLocalVariable()
    {
        NotAnnotatedClass var;
    }

    public void notAnnotatedApiAsMember()
    {
        classWithAnnotatedMember.notAnnotated();
    }

    public NotAnnotatedClass notAnnotatedClassAsReturnType()
    {
        return null;
    }

    public static class ExtendingNotAnnotatedApi
            extends NotAnnotatedClass
    {}
}
