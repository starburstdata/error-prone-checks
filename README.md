# Starburst Error Prone Bug Checkers

Collection of [Error Prone](https://github.com/google/error-prone) bug checkers for the
[Trino](https://trino.io/) ecosystem, e.g. connectors, functions, authenticators, and other
extensions.

## Bug Checkers

* `DeprecatedApi` - Check for usages of `@Deprecated` APIs.
* `TrinoExperimentalSpi` - Check for usages of Trino `@Experimental` SPIs.
* `PreferGuavaImmutableCollections` - Prefer Guava immutable collections over JDK immutable factories and collectors.

The API usage checkers can be fine-tuned with the following flags:

* `-XepOpt:<CheckerName>:BasePackages=io.trino.spi,...` - check for usage of APIs from these base
* packages only.
* `-XepOpt:<CheckerName>:IgnoredPackages=io.trino.spi,...` - ignore usage of APIs from these
* packages
* `-XepOpt:<CheckerName>:IgnoredTypes=io.trino.spi.MyClass,...` - ignore usage of APIs from these
* classes

Individual warnings can be suppressed with the `@SuppressWarnings("<CheckerName>")` annotation. For
the `DeprecatedApi` checker, `@SuppressWarnings({"deprecation", "DeprecatedApi"})` suppresses both
compiler and error-prone checker warnings. This is necessary as `DeprecatedApi` can be more
restrictive than the compiler deprecation check.

## Usage

To use the checkers configure your project to build with the Error Prone Java compiler and add
`error-prone-checks` annotation processor.

To customize that pass an additional compiler argument, e.g. `-Xep:TrinoExperimentalSpi:ERROR`.
Allowed values are: `ERROR`, `WARN`, `OFF`, `DEFAULT`.

### Prefer Guava immutable collections

This checker requires an artifact built from this revision (`6-SNAPSHOT` during
development). Publish a new release and update the consumer version before enabling it in CI.

Enable `-Xep:PreferGuavaImmutableCollections:ERROR` to reject `List.of/copyOf`,
`Set.of/copyOf`, `Map.of/ofEntries/copyOf`, and `Collectors.toUnmodifiableList/Set/Map`.
Resolved methods are checked, including static imports and method references. The checker
encourages consistent collection implementations because copying between JDK and Guava
immutable collections generally creates new storage. It does not track allocation provenance.

Suggested fixes cover list factories, `List.copyOf` calls and method references, and
`Collectors.toUnmodifiableList` calls directly passed to `Stream.collect`. Standalone
collectors receive diagnostics without fixes because their declared generic result types
may require `List` rather than `ImmutableList`. Array overloads use `ImmutableList.copyOf(array)`
when the JDK factory expands the array; explicit array elements remain array elements.
Calls inside inferred `var` initializers receive no fix because changing the expression's
static type can break generic containers, such as `Optional<List<T>>`. Guava and JDK lists
also differ in null query handling; review callers that depend on those behaviors.
Set and map APIs receive diagnostics without automatic fixes because duplicate handling,
iteration order, or merge semantics need review. Factory method references also receive
no fix because their target types can change overload selection. `Stream.toList()` is outside
this checker: unlike Guava immutable lists, it accepts null elements.

Run Error Prone's [patch mode](https://errorprone.info/docs/patching) to apply supported fixes.
Append these flags to the project's existing `-Xplugin:ErrorProne` compiler argument:

```text
-Xep:PreferGuavaImmutableCollections:WARN
-XepPatchChecks:PreferGuavaImmutableCollections
-XepPatchLocation:IN_PLACE
```

Only compiled sources receive fixes. Review the diff, run the project's formatter to remove
unused imports, and compile the rewritten sources again.
Use `@SuppressWarnings("PreferGuavaImmutableCollections")` for intentional exceptions.

### Maven

In `pom.xml`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        ...
        <annotationProcessorPaths>
            ...
            <path>
                <groupId>io.starburst.errorprone</groupId>
                <artifactId>error-prone-checks</artifactId>
                <version>5</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

# Development

Requires JDK 25 and [mvnd](https://github.com/apache/maven-mvnd) 1.0.6 or newer.
Run `mvnd verify -P ci` to build, test, and run the CI checks.

The project uses the [Airbase Airlift](https://github.com/airlift/airbase/) framework, mostly to re-use useful Maven
templates and patterns. This includes the `maven-gpg-plugin`, which is only enabled during the release by the profile
`oss-release`.

## Release process

Releases are published to [Sonatype Central Portal](https://central.sonatype.org/publish/publish-portal-guide/)
using Airbase's `oss-release` profile and Njord publishing extension.

Prerequisites:

* A Central Portal account with access to this project's namespace and a generated user token.
* A GPG private key for signing release artifacts, with its public key available from a keyserver.
* `mvnd` and JDK 25 or newer.

Configure the Central Portal token in your local `~/.m2/settings.xml`:

```xml
<settings>
    <servers>
        <server>
            <id>sonatype-central-portal</id>
            <username><!-- token username --></username>
            <password><!-- token password --></password>
        </server>
    </servers>
</settings>
```

Configure GPG according to the [Maven GPG Plugin documentation](https://maven.apache.org/plugins/maven-gpg-plugin/usage.html).
Keep signing credentials outside the repository.

Prepare and publish the release:

```shell
mvnd release:clean release:prepare -P oss-release
mvnd release:perform -P oss-release
```

The release profile signs artifacts and configures automatic Central Portal publishing.
Review the release commits and tag before pushing them. Update the consumer version shown in
[this README](#maven) after the release is available from Maven Central.
