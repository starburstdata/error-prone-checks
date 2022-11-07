# Starburst Error Prone Bug Checkers

Collection of [Error Prone](https://github.com/google/error-prone) bug checkers for the
[Trino](https://trino.io/) ecosystem, e.g. connectors, functions, authenticators, and other
extensions.

## Bug Checkers

* `DeprecatedApi` - Check for usages of `@Deprecated` APIs.
* `TrinoExperimentalSpi` - Check for usages of Trino `@Experimental` SPIs.

Both checkers can be fine-tuned with the following flags:

* `-XepOpt:<CheckerName>:BasePackages=io.trino.spi,...` - check for usage of APIs from these base
  packages only.
* `-XepOpt:<CheckerName>:IgnoredPackages=io.trino.spi,...` - ignore usage of APIs from these
  packages
* `-XepOpt:<CheckerName>:IgnoredTypes=io.trino.spi.MyClass,...` - ignore usage of APIs from these
  classes

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

The project uses the [Airbase Airlift](https://github.com/airlift/airbase/) framework, mostly to re-use useful Maven
templates and patterns. This includes the `maven-gpg-plugin`, which is only enabled during the release by the profile
`oss-release`.

## Release process

The project is deployed to OSS Sonatype repository. The release process is manual, executed from the local development
environment, and it uses the standard Maven Release Plugin.

Prerequisites:

* An account on [Sonatype Nexus Repository Manager](https://oss.sonatype.org/)
* A GPG private key for signing the release artifacts

### Sonatype account

Once you have the Sonatype account, you need to configure Maven to use this account's credentials whe uploading
artifacts. In your local `~/.m2/settings.xml` file add the following section:

```xml
<settings>
    <!-- ... -->
    <servers>
        <!-- Sonatype Maven Release Repository -->
        <server>
            <id>ossrh</id>
            <username><!-- your login here --></username>
            <password><!-- your password here --></password>
        </server>
    </servers>
</settings>
```

This is a local file in you development environment, so these credentials are not published anywhere.

### GPG private key

You can use this [guide from Sonatype](https://central.sonatype.org/publish/requirements/gpg/) to help you set things
up. Note that the signing key can be any key for which the public part is reachable from a keyserver
(`keyserver.ubuntu.com` by default), even a personal one, or one created specifically for this purpose.

For this to work, though, some more things need to be configured in local Maven.

First, in case you have the `oss-release` profile already in `~/.m2/settings.xml`, you need to set the `gpg.skip`
property to `false`:

```xml
<settings>
    <!-- ... -->
    <profiles>
        <profile>
            <!-- this profile name is defined by Airbase -->
            <id>oss-release</id>
            <properties>
                <gpg.skip>false</gpg.skip>
            </properties>
        </profile>
    </profiles>
</settings>
```

Second, Maven needs to be able to read the password to the key during build. You can follow the instructions in the
[plugin documentation](https://maven.apache.org/plugins/maven-gpg-plugin/usage.html).

Yet another way, but not really secure (as it has the password in plain text in a local file), is to configure the
password in the `~/.m2/settings.xml` file in the `oss-release` profile used by the release process in Airbase:

```xml
<settings>
    <!-- ... -->
    <profiles>
        <profile>
            <!-- this profile name is defined by Airbase -->
            <id>oss-release</id>
            <properties>
                <gpg.passphrase><!-- your password to the private key here --></gpg.passphrase>
            </properties>
        </profile>
    </profiles>
</settings>
```

### Doing the release

Once the above things are configured, this is basically a matter of executing the following commands:

```shell
$ ./mvnw release:clean release:prepare
$ ./mvnw release:perform
```

After they finish, you'll have two additional commits in you local git clone which switch over the project's version to
a new one. Update [this readme](#maven) to mention the new version and create a pull request with these commits.

The new release will end up in a staging repository in Sonatype and needs to be manually promoted. Log in to your
account, and follow [these instructions from Sonatype](https://central.sonatype.org/publish/release/).
