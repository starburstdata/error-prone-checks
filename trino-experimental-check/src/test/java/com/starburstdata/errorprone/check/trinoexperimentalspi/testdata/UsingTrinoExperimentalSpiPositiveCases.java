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

import io.trino.spi.trinoexperimentalspi.AnnotatedClass;
import io.trino.spi.trinoexperimentalspi.ClassWithAnnotatedMember;

import java.util.List;

public class UsingTrinoExperimentalSpiPositiveCases
{
    // BUG: Diagnostic contains: Do not use Trino experimental SPI.
    private final Object instantiationOfAnnotatedClass = new AnnotatedClass();

    // BUG: Diagnostic contains: Do not use Trino experimental SPI.
    private AnnotatedClass annotatedClassAsField;

    private final ClassWithAnnotatedMember classWithAnnotatedMember = new ClassWithAnnotatedMember();

    // BUG: Diagnostic contains: Do not use Trino experimental SPI.
    private AnnotatedClass annotatedApiAsInstanceVariable;

    // BUG: Diagnostic contains: Do not use Trino experimental SPI.
    public void annotatedApiAsParameter(AnnotatedClass param)
    {}

    // BUG: Diagnostic contains: Do not use Trino experimental SPI.
    public void annotatedApiAsTypeArgument(List<AnnotatedClass> param)
    {}

    public void annotatedApiAsLocalVariable()
    {
        // BUG: Diagnostic contains: Do not use Trino experimental SPI.
        AnnotatedClass var;
    }

    public void annotatedApiAsMember()
    {
        // BUG: Diagnostic contains: Do not use Trino experimental SPI.
        classWithAnnotatedMember.annotated();
    }

    // BUG: Diagnostic contains: Do not use Trino experimental SPI.
    public AnnotatedClass annotatedClassAsReturnType()
    {
        return null;
    }

    public static class ExtendingAnnotatedApi
            // BUG: Diagnostic contains: Do not use Trino experimental SPI.
            extends AnnotatedClass
    {}
}
