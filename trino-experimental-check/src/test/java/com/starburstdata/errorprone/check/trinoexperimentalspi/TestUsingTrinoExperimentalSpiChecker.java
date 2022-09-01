/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */

package com.starburstdata.errorprone.check.trinoexperimentalspi;

import com.google.errorprone.CompilationTestHelper;
import org.testng.annotations.Test;

@Test
public class TestUsingTrinoExperimentalSpiChecker
{
    @Test
    public void usingExperimentalSpiPositiveCases()
    {
        CompilationTestHelper compilationTestHelper = CompilationTestHelper.newInstance(UsingTrinoExperimentalSpiChecker.class, getClass());
        compilationTestHelper.addSourceFile("UsingTrinoExperimentalSpiPositiveCases.java").doTest();
    }

    @Test
    public void usingExperimentalSpiNegativeCases()
    {
        CompilationTestHelper compilationTestHelper = CompilationTestHelper.newInstance(UsingTrinoExperimentalSpiChecker.class, getClass());
        compilationTestHelper.addSourceFile("UsingTrinoExperimentalSpiNegativeCases.java").doTest();
    }
}
