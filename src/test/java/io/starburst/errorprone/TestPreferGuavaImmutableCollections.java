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

import com.google.errorprone.BugCheckerRefactoringTestHelper;
import com.google.errorprone.CompilationTestHelper;
import org.gaul.modernizer_maven_annotations.SuppressModernizer;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.function.Function.identity;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPreferGuavaImmutableCollections
{
    @Test
    public void testFactoriesAndCollectors()
    {
        compilationTestHelper()
                .addSourceLines("Test.java",
                        """
                        import java.util.Collection;
                        import java.util.List;
                        import java.util.Map;
                        import java.util.Set;
                        import java.util.stream.Collectors;
                        import static java.util.List.of;
                        class Test {
                            void test(Collection<String> values, Map<String, String> map) {
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                List.of();
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                java.util.List.of("a", "b");
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                of("a");
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                List.copyOf(values);
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                Set.of();
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                Set.of("a");
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                java.util.Set.of("a", "b");
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                Set.copyOf(values);
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                Map.of();
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                Map.of("a", "b");
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                java.util.Map.of("a", "b");
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                Map.ofEntries(Map.entry("a", "b"));
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                Map.copyOf(map);
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                values.stream().collect(Collectors.toUnmodifiableList());
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                values.stream().collect(Collectors.toUnmodifiableSet());
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                values.stream().collect(Collectors.toUnmodifiableMap(x -> x, x -> x));
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                values.stream().collect(Collectors.toUnmodifiableMap(x -> x, x -> x, (a, b) -> a));
                            }
                        }
                        """)
                .addSourceLines("SetImports.java",
                        """
                        import java.util.Collection;
                        import static java.util.Set.copyOf;
                        import static java.util.Set.of;
                        class SetImports {
                            void test(Collection<String> values) {
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                of();
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                of("a", "b");
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                copyOf(values);
                            }
                        }
                        """)
                .addSourceLines("MapImports.java",
                        """
                        import java.util.Map;
                        import static java.util.Map.copyOf;
                        import static java.util.Map.of;
                        import static java.util.Map.ofEntries;
                        class MapImports {
                            void test(Map<String, String> values) {
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                of();
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                of("a", "b");
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                ofEntries(Map.entry("a", "b"));
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                copyOf(values);
                            }
                        }
                        """)
                .addSourceLines("CollectorImports.java",
                        """
                        import java.util.Collection;
                        import static java.util.stream.Collectors.toUnmodifiableList;
                        import static java.util.stream.Collectors.toUnmodifiableMap;
                        import static java.util.stream.Collectors.toUnmodifiableSet;
                        class CollectorImports {
                            void test(Collection<String> values) {
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                values.stream().collect(toUnmodifiableList());
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                values.stream().collect(toUnmodifiableSet());
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                values.stream().collect(toUnmodifiableMap(x -> x, x -> x));
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                values.stream().collect(toUnmodifiableMap(x -> x, x -> x, (a, b) -> a));
                            }
                        }
                        """)
                .doTest();
    }

    @Test
    public void testMethodReferences()
    {
        compilationTestHelper()
                .addSourceLines("Test.java",
                        """
                        import java.util.Collection;
                        import java.util.List;
                        import java.util.Map;
                        import java.util.Set;
                        import java.util.function.BiFunction;
                        import java.util.function.Function;
                        import java.util.function.Supplier;
                        import java.util.stream.Collector;
                        import java.util.stream.Collectors;
                        class Test {
                            // BUG: Diagnostic contains: Prefer Guava immutable collections
                            Function<String, List<String>> list = List::of;
                            // BUG: Diagnostic contains: Prefer Guava immutable collections
                            Function<Collection<String>, List<String>> listCopy = List::copyOf;
                            // BUG: Diagnostic contains: Prefer Guava immutable collections
                            Function<String, Set<String>> set = Set::of;
                            // BUG: Diagnostic contains: Prefer Guava immutable collections
                            Function<Collection<String>, Set<String>> setCopy = Set::copyOf;
                            // BUG: Diagnostic contains: Prefer Guava immutable collections
                            BiFunction<String, String, Map<String, String>> map = Map::of;
                            // BUG: Diagnostic contains: Prefer Guava immutable collections
                            Function<Map<String, String>, Map<String, String>> mapCopy = Map::copyOf;
                            // BUG: Diagnostic contains: Prefer Guava immutable collections
                            Function<Map.Entry<String, String>[], Map<String, String>> entries = Map::ofEntries;
                            // BUG: Diagnostic contains: Prefer Guava immutable collections
                            Supplier<Collector<String, ?, List<String>>> collector = Collectors::toUnmodifiableList;
                        }
                        """)
                .doTest();
    }

    @Test
    public void testAllowedAndSuppressedUses()
    {
        compilationTestHelper()
                .addSourceLines("Test.java",
                        """
                        import com.google.common.collect.ImmutableList;
                        import com.google.common.collect.ImmutableMap;
                        import com.google.common.collect.ImmutableSet;
                        import java.util.ArrayList;
                        import java.util.Collections;
                        import java.util.List;
                        import java.util.stream.Collectors;
                        import java.util.stream.Stream;
                        class Test {
                            void test() {
                                ImmutableList.of("a");
                                ImmutableSet.of("a");
                                ImmutableMap.of("a", "b");
                                Collections.unmodifiableList(new ArrayList<>());
                                Stream.of("a").toList();
                                var nested = java.util.Optional.<List<String>>of(ImmutableList.of("a"));
                                java.util.Optional<List<String>> result = nested;
                                Stream.of("a").collect(Collectors.toList());
                                Other.of();
                                Other.copyOf();
                            }
                            @SuppressWarnings("PreferGuavaImmutableCollections")
                            void suppressed() {
                                List.of("a");
                                java.util.function.Function<String, List<String>> factory = List::of;
                            }
                            static class Other {
                                static void of() {}
                                static void copyOf() {}
                            }
                        }
                        """)
                .doTest();
    }

    @Test
    public void testListFixes()
    {
        refactoringTestHelper()
                .addInputLines("Test.java",
                        """
                        import java.util.Collection;
                        import java.util.List;
                        import java.util.Optional;
                        class Test {
                            void test(Collection<String> values, String[] array, Object[] objects, int[] primitives) {
                                List.of();
                                List.of("a", "b");
                                List.of(array);
                                List.of(objects);
                                List.of(primitives);
                                List.of((Object) array);
                                List.<String[]>of(array);
                                List.<Object>of(array);
                                List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l");
                                Optional<List<String>> nested = Optional.of(List.of("a"));
                                List.copyOf(values);
                                java.util.List.of("a");
                                values.stream().collect(java.util.stream.Collectors.toUnmodifiableList());
                            }
                        }
                        """)
                .addOutputLines("Test.java",
                        """
                        import com.google.common.collect.ImmutableList;
                        import java.util.Collection;
                        import java.util.List;
                        import java.util.Optional;
                        class Test {
                            void test(Collection<String> values, String[] array, Object[] objects, int[] primitives) {
                                ImmutableList.of();
                                ImmutableList.of("a", "b");
                                ImmutableList.copyOf(array);
                                ImmutableList.copyOf(objects);
                                ImmutableList.of(primitives);
                                ImmutableList.of((Object) array);
                                ImmutableList.<String[]>of(array);
                                ImmutableList.<Object>copyOf(array);
                                ImmutableList.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l");
                                Optional<List<String>> nested = Optional.of(ImmutableList.of("a"));
                                ImmutableList.copyOf(values);
                                ImmutableList.of("a");
                                values.stream().collect(ImmutableList.toImmutableList());
                            }
                        }
                        """)
                .doTest();
    }

    @Test
    public void testStaticImportsAndNameConflicts()
    {
        refactoringTestHelper()
                .addInputLines("Test.java",
                        """
                        import java.util.List;
                        import static java.util.List.of;
                        class Test {
                            List<String> values = of("a", "b");
                            static class ImmutableList {}
                        }
                        """)
                .addOutputLines("Test.java",
                        """
                        import java.util.List;
                        import static java.util.List.of;
                        class Test {
                            List<String> values = com.google.common.collect.ImmutableList.of("a", "b");
                            static class ImmutableList {}
                        }
                        """)
                .doTest();
    }

    @Test
    public void testCopyMethodReferenceFix()
    {
        refactoringTestHelper()
                .addInputLines("Test.java",
                        """
                        import java.util.Collection;
                        import java.util.List;
                        import java.util.function.Function;
                        class Test {
                            Function<Collection<String>, List<String>> factory = List::copyOf;
                        }
                        """)
                .addOutputLines("Test.java",
                        """
                        import com.google.common.collect.ImmutableList;
                        import java.util.Collection;
                        import java.util.List;
                        import java.util.function.Function;
                        class Test {
                            Function<Collection<String>, List<String>> factory = ImmutableList::copyOf;
                        }
                        """)
                .doTest();
    }

    @Test
    public void testGenericReceiverInferenceIsNotRewritten()
    {
        refactoringTestHelper()
                .addInputLines("Test.java",
                        """
                        import java.util.ArrayList;
                        import java.util.List;
                        import java.util.Optional;
                        import java.util.Set;
                        import java.util.stream.Stream;
                        class Test {
                            void test(Stream<String> values, Stream<Set<String>> sets) {
                                List<List<String>> mapped = values.map(value -> List.of(value)).toList();
                                List<List<String>> copied = sets.map(List::copyOf).toList();
                                List<String> optional = Optional.of(List.of("a")).orElseGet(ArrayList::new);
                            }
                        }
                        """)
                .expectUnchanged()
                .doTest();
    }

    @Test
    public void testGenericReceiverWithCompatibleTargetIsRewritten()
    {
        refactoringTestHelper()
                .addInputLines("Test.java",
                        """
                        import java.util.List;
                        import java.util.Optional;
                        import java.util.stream.Collectors;
                        import java.util.stream.Stream;
                        class Test {
                            void test() {
                                List<List<String>> nested = Stream.of(List.of("a")).collect(Collectors.toList());
                                List<List<String>> mapped = Stream.of("a").<List<String>>map(value -> List.of(value)).toList();
                                List<String> optional = Optional.<List<String>>of(List.of("a")).orElseThrow();
                            }
                        }
                        """)
                .addOutputLines("Test.java",
                        """
                        import com.google.common.collect.ImmutableList;
                        import java.util.List;
                        import java.util.Optional;
                        import java.util.stream.Collectors;
                        import java.util.stream.Stream;
                        class Test {
                            void test() {
                                List<List<String>> nested = Stream.of(ImmutableList.of("a")).collect(Collectors.toList());
                                List<List<String>> mapped = Stream.of("a").<List<String>>map(value -> ImmutableList.of(value)).toList();
                                List<String> optional = Optional.<List<String>>of(ImmutableList.of("a")).orElseThrow();
                            }
                        }
                        """)
                .doTest();
    }

    @Test
    public void testInferredGenericContainerIsNotRewritten()
    {
        refactoringTestHelper()
                .addInputLines("Test.java",
                        """
                        import java.util.List;
                        import java.util.Optional;
                        class Test {
                            void test() {
                                var nested = Optional.of(List.of("a"));
                                Optional<List<String>> result = nested;
                            }
                        }
                        """)
                .expectUnchanged()
                .doTest();
    }

    @Test
    public void testUnsupportedFixes()
    {
        refactoringTestHelper()
                .addInputLines("Test.java",
                        """
                        import java.util.Collection;
                        import java.util.List;
                        import java.util.Map;
                        import java.util.Set;
                        import java.util.function.Function;
                        import java.util.stream.Collector;
                        import java.util.stream.Collectors;
                        class Test {
                            Function<String[], List<String>> arrayFactory = List::of;
                            Collector<String, ?, List<String>> listCollector = Collectors.toUnmodifiableList();
                            void test(Collection<String> values, Map<String, String> map) {
                                Set.of("a", "a");
                                Set.copyOf(values);
                                Map.ofEntries(Map.entry("a", "b"));
                                Map.copyOf(map);
                                values.stream().collect(Collectors.toUnmodifiableSet());
                                values.stream().collect(Collectors.toUnmodifiableMap(x -> x, x -> x, (a, b) -> a));
                            }
                        }
                        """)
                .expectUnchanged()
                .doTest();
    }

    @Test
    public void testUnicodeEscapedMethodNameIsNotRewritten()
    {
        // Unicode escapes spell "of", with different source offsets and no google-java-format support.
        compilationTestHelper()
                .addSourceLines("Test.java",
                        """
                        import java.util.List;
                        class Test {
                            void test() {
                                // BUG: Diagnostic contains: Prefer Guava immutable collections
                                List.\\u006f\\u0066("a");
                            }
                        }
                        """)
                .doTest();
        refactoringTestHelper()
                .allowFormattingErrors()
                .addInputLines("Test.java",
                        """
                        import java.util.List;
                        class Test {
                            void test() {
                                List.\\u006f\\u0066("a");
                            }
                        }
                        """)
                .expectUnchanged()
                .doTest();
    }

    @Test
    @SuppressModernizer // Stream.toList() accepts nulls, so it cannot test this contract.
    public void testListCollectorsRejectNullElements()
    {
        var jdkCollector = Collectors.<String>toUnmodifiableList();
        assertThrows(NullPointerException.class, () -> Stream.of("a", null).collect(jdkCollector));
        assertThrows(NullPointerException.class, () -> Stream.of("a", null).collect(toImmutableList()));
    }

    @Test
    public void testSetCollectorsRejectNullElements()
    {
        assertThrows(NullPointerException.class, () -> Stream.of("a", null).collect(Collectors.toUnmodifiableSet()));
        assertThrows(NullPointerException.class, () -> Stream.of("a", null).collect(toImmutableSet()));
    }

    @Test
    public void testMapCollectorsRejectNullKeysAndValues()
    {
        assertThrows(NullPointerException.class, () -> Stream.of("a").collect(Collectors.toUnmodifiableMap(_ -> null, identity())));
        assertThrows(NullPointerException.class, () -> Stream.of("a").collect(toImmutableMap(_ -> null, identity())));
        assertThrows(NullPointerException.class, () -> Stream.of("a").collect(Collectors.toUnmodifiableMap(identity(), _ -> null)));
        assertThrows(NullPointerException.class, () -> Stream.of("a").collect(toImmutableMap(identity(), _ -> null)));
        assertThrows(NullPointerException.class, () -> Stream.of("a").collect(Collectors.toUnmodifiableMap(_ -> null, identity(), (first, _) -> first)));
        assertThrows(NullPointerException.class, () -> Stream.of("a").collect(toImmutableMap(_ -> null, identity(), (first, _) -> first)));
        assertThrows(NullPointerException.class, () -> Stream.of("a").collect(Collectors.toUnmodifiableMap(identity(), _ -> null, (first, _) -> first)));
        assertThrows(NullPointerException.class, () -> Stream.of("a").collect(toImmutableMap(identity(), _ -> null, (first, _) -> first)));
    }

    private CompilationTestHelper compilationTestHelper()
    {
        return CompilationTestHelper.newInstance(PreferGuavaImmutableCollections.class, getClass());
    }

    private BugCheckerRefactoringTestHelper refactoringTestHelper()
    {
        return BugCheckerRefactoringTestHelper.newInstance(PreferGuavaImmutableCollections.class, getClass());
    }
}
