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

public class TestRequireNonNullMessageChecker
{
    private CompilationTestHelper compilationTestHelper()
    {
        return CompilationTestHelper.newInstance(RequireNonNullMessageChecker.class, getClass());
    }

    @Test
    public void testNegativeCases()
    {
        compilationTestHelper().addSourceFile("RequireNonNullMessageNegativeCases.java").doTest();
    }

    @Test
    public void testPositiveCases()
    {
        compilationTestHelper().addSourceFile("RequireNonNullMessagePositiveCases.java").doTest();
    }
}
