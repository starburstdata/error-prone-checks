# Starburst Error Prone Bug Checkers

Collection of [Error Prone](https://github.com/google/error-prone) bug checkers for the
[Trino](https://trino.io/) ecosystem, e.g. connectors, functions, authenticators, and other
extensions.

## Bug Checkers

* `DeprecatedApi` - Check for usages of `@Deprecated` APIs.
* `TrinoExperimentalSpi` - Check for usages of Trino `@Experimental` SPIs.

Both checkers can be fine-tuned with the following flags:

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
Allowed values are: `ERROR`, `WARNING`, `SUGGESTION`, `OFF`.

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
                <version>4</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```
