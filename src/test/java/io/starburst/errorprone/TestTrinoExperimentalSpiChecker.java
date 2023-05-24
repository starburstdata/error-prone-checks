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
    private CompilationTestHelper compilationTestHelper()
    {
        return CompilationTestHelper.newInstance(TrinoExperimentalSpiChecker.class, getClass());
    }

    @Test
    public void testNegativeCases()
    {
        compilationTestHelper().addSourceFile("TrinoExperimentalSpiNegativeCases.java").doTest();
    }

    @Test
    public void testNegativeCasesSuppressed()
    {
        compilationTestHelper().addSourceFile("TrinoExperimentalSpiNegativeCasesSuppressed.java").doTest();
    }

    @Test
    public void testPositiveCases()
    {
        compilationTestHelper().addSourceFile("TrinoExperimentalSpiPositiveCases.java").doTest();
    }

    @Test
    public void testNegativeCasesWithIgnoredPackages()
    {
        // SPI package is ignored
        compilationTestHelper()
                .setArgs("-XepOpt:TrinoExperimentalSpi:IgnoredPackages=io.trino.spi.experimental")
                .addSourceFile("TrinoExperimentalSpiNegativeCasesIgnored.java")
                .doTest();

        // SPI package matches base but is ignored
        compilationTestHelper()
                .setArgs(
                        "-XepOpt:TrinoExperimentalSpi:BasePackages=io.trino.spi",
                        "-XepOpt:TrinoExperimentalSpi:IgnoredPackages=io.trino.spi.experimental")
                .addSourceFile("TrinoExperimentalSpiNegativeCasesIgnored.java")
                .doTest();
    }

    @Test
    public void testNegativeCasesWithIgnoredTypes()
    {
        // SPI class is ignored
        compilationTestHelper()
                .setArgs("-XepOpt:TrinoExperimentalSpi:IgnoredTypes=io.trino.spi.experimental.ExperimentalClass,io.trino.spi.experimental.ClassWithExperimentalMember")
                .addSourceFile("TrinoExperimentalSpiNegativeCasesIgnored.java")
                .doTest();

        // SPI package matches base but the class is ignored
        compilationTestHelper()
                .setArgs(
                        "-XepOpt:TrinoExperimentalSpi:BasePackages=io.trino.spi",
                        "-XepOpt:TrinoExperimentalSpi:IgnoredTypes=io.trino.spi.experimental.ExperimentalClass,io.trino.spi.experimental.ClassWithExperimentalMember")
                .addSourceFile("TrinoExperimentalSpiNegativeCasesIgnored.java")
                .doTest();
    }

    @Test
    public void testNegativeCasesWithBasePackages()
    {
        // SPI package doesn't match base
        compilationTestHelper()
                .setArgs("-XepOpt:TrinoExperimentalSpi:BasePackages=io.bar.foo")
                .addSourceLines(
                        "Test.java",
                        """
                                import io.trino.spi.experimental.ExperimentalClass;
                                public class Test {
                                    private ExperimentalClass asField;
                                }
                                """)
                .doTest();
    }

    @Test
    public void testPositiveCasesWithBasePackages()
    {
        // 1 of the 2 SPI packages match base
        compilationTestHelper()
                .setArgs("-XepOpt:TrinoExperimentalSpi:BasePackages=io.trino.spi.experimental")
                .addSourceLines(
                        "Test.java",
                        """
                                import io.trino.spi.experimental.ExperimentalClass;
                                import io.trino.spi.ignored.IgnoredExperimentalClass;
                                public class Test {
                                    // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs from 'io.trino.spi.experimental'.
                                    private ExperimentalClass asField1;
                                    private IgnoredExperimentalClass asField2;
                                }
                                """)
                .doTest();

        // Both SPI packages match base
        compilationTestHelper()
                .setArgs("-XepOpt:TrinoExperimentalSpi:BasePackages=io.trino.spi.experimental,io.trino.spi.ignored")
                .addSourceLines(
                        "Test.java",
                        """
                                import io.trino.spi.experimental.ExperimentalClass;
                                import io.trino.spi.ignored.IgnoredExperimentalClass;
                                public class Test {
                                    // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs from 'io.trino.spi.experimental'.
                                    private ExperimentalClass asField1;
                                    // BUG: Diagnostic contains: Do not use Trino @Experimental SPIs from 'io.trino.spi.ignored'.
                                    private IgnoredExperimentalClass asField2;
                                }
                                """)
                .doTest();
    }
}
