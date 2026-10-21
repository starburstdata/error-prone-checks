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
