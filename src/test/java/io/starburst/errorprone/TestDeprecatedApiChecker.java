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
public class TestDeprecatedApiChecker
{
    private CompilationTestHelper compilationTestHelper()
    {
        return CompilationTestHelper.newInstance(DeprecatedApiChecker.class, getClass());
    }

    @Test
    public void testNegativeCases()
    {
        compilationTestHelper().addSourceFile("DeprecatedApiNegativeCases.java").doTest();
    }

    @Test
    public void testNegativeCasesSuppressed()
    {
        compilationTestHelper().addSourceFile("DeprecatedApiNegativeCasesSuppressed.java").doTest();
    }

    @Test
    public void testPositiveCases()
    {
        compilationTestHelper().addSourceFile("DeprecatedApiPositiveCases.java").doTest();
    }

    @Test
    public void testNegativeCasesWithIgnoredPackages()
    {
        String path = "Test.java";
        String lines = """
                import io.trino.spi.ignored.IgnoredDeprecatedClass;
                public class Test {
                    private IgnoredDeprecatedClass asField;
                }
                """;

        // API package is ignored
        compilationTestHelper()
                .setArgs("-XepOpt:DeprecatedApi:IgnoredPackages=io.trino.spi.ignored")
                .addSourceLines(path, lines)
                .doTest();

        // API package matches base but is ignored
        compilationTestHelper()
                .setArgs(
                        "-XepOpt:DeprecatedApi:BasePackages=io.trino.spi",
                        "-XepOpt:DeprecatedApi:IgnoredPackages=io.trino.spi.ignored")
                .addSourceLines(path, lines)
                .doTest();
    }

    @Test
    public void testNegativeCasesWithIgnoredTypes()
    {
        String path = "Test.java";
        String lines = """
                import io.trino.spi.ignored.IgnoredDeprecatedClass;
                public class Test {
                    private IgnoredDeprecatedClass asField;
                }
                """;

        // API package is ignored
        compilationTestHelper()
                .setArgs("-XepOpt:DeprecatedApi:IgnoredTypes=io.trino.spi.ignored.IgnoredDeprecatedClass")
                .addSourceLines(path, lines)
                .doTest();

        // API package matches base but is ignored
        compilationTestHelper()
                .setArgs(
                        "-XepOpt:DeprecatedApi:BasePackages=io.trino.spi",
                        "-XepOpt:DeprecatedApi:IgnoredTypes=io.trino.spi.ignored.IgnoredDeprecatedClass")
                .addSourceLines(path, lines)
                .doTest();
    }

    @Test
    public void testNegativeCasesWithBasePackages()
    {
        // API package doesn't match base
        compilationTestHelper()
                .setArgs("-XepOpt:DeprecatedApi:BasePackages=io.bar.foo")
                .addSourceLines(
                        "Test.java",
                        """
                                import io.trino.spi.deprecated.DeprecatedClass;
                                public class Test {
                                    private DeprecatedClass asField;
                                }
                                """)
                .doTest();
    }

    @Test
    public void testPositiveCasesWithBasePackages()
    {
        // 1 of the 2 API packages match base
        compilationTestHelper()
                .setArgs("-XepOpt:DeprecatedApi:BasePackages=io.trino.spi.deprecated")
                .addSourceLines(
                        "Test.java",
                        """
                                import io.trino.spi.deprecated.DeprecatedClass;
                                import io.trino.spi.ignored.IgnoredDeprecatedClass;
                                public class Test {
                                    // BUG: Diagnostic contains: Do not use @Deprecated APIs from 'io.trino.spi.deprecated'.
                                    private DeprecatedClass asField1;
                                    private IgnoredDeprecatedClass asField2;
                                }
                                """)
                .doTest();

        // Both API packages match base
        compilationTestHelper()
                .setArgs("-XepOpt:DeprecatedApi:BasePackages=io.trino.spi.deprecated,io.trino.spi.ignored")
                .addSourceLines(
                        "Test.java",
                        """
                                import io.trino.spi.deprecated.DeprecatedClass;
                                import io.trino.spi.ignored.IgnoredDeprecatedClass;
                                public class Test {
                                    // BUG: Diagnostic contains: Do not use @Deprecated APIs from 'io.trino.spi.deprecated'.
                                    private DeprecatedClass asField1;
                                    // BUG: Diagnostic contains: Do not use @Deprecated APIs from 'io.trino.spi.ignored'.
                                    private IgnoredDeprecatedClass asField2;
                                }
                                """)
                .doTest();
    }
}
