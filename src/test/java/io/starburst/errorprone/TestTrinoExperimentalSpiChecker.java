/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */

package io.starburst.errorprone;

import com.google.errorprone.CompilationTestHelper;
import org.testng.annotations.Test;

@Test
public class TestTrinoExperimentalSpiChecker
{
    @Test
    public void testNegativeCases()
    {
        CompilationTestHelper compilationTestHelper = CompilationTestHelper.newInstance(TrinoExperimentalSpiChecker.class, getClass());
        compilationTestHelper.addSourceFile("TrinoExperimentalSpiNegativeCases.java").doTest();
    }

    @Test
    public void testPositiveCases()
    {
        CompilationTestHelper compilationTestHelper = CompilationTestHelper.newInstance(TrinoExperimentalSpiChecker.class, getClass());
        compilationTestHelper.addSourceFile("TrinoExperimentalSpiPositiveCases.java").doTest();
    }
}
