# Starburst Error Prone Bug Checkers

Collection of [Error Prone](https://github.com/google/error-prone) bug checkers for the
[Trino](https://trino.io/) ecosystem, e.g. connectors, functions, authenticators, and other
extensions.

## Bug Checkers

### Checkers for annotation-related issues

* `DeprecatedApi` - Check for usages of `@Deprecated` APIs.
* `TrinoExperimentalSpi` - Check for usages of Trino `@Experimental` SPIs.

Both checkers can be fine-tuned with the following flags:

* `-XepOpt:<CheckerName>:BasePackages=io.trino.spi,...` - check for usage of APIs from these base
  packages only.
* `-XepOpt:<CheckerName>:IgnoredPackages=io.trino.spi,...` - ignore usage of APIs from these
  packages
* `-XepOpt:<CheckerName>:IgnoredTypes=io.trino.spi.MyClass,...` - ignore usage of APIs from these
  classes

### Checker for issues with `Objects::requireNonNull`

* `RequireNonNullMessage` - Make sure that the message passed as the second argument is not
  malformed

This check focuses on cases of simple typos or mismatches of the error message with the thing being
checked. It looks for calls to `Objects#requireNonNull` with a message which is a literal string
matching the pattern:

```
<identifier>[additional information]["is null"][more additional information]
```

The "is null" part can also be one of some other variants, like "is required", "are missing",
"can't be null" etc.

There are two places where some additional information can be placed:

* After the identifier:

```
<identifier> for something is null
```

* After the "is null" pattern:

```
<identifier> is null: this is bad
```

Or it can be both. Note that the "is null" part is also optional, so things like
`requireNonNull(parameter, "parameter")` are also allowed.

If a message matching the above pattern is found, it then checks if the `<identifier>` is identical
to the identifier passed as the first argument to `requireNonNull` and will trigger if they're
not. It will ignore anything more complex than a plain identifier. It will also ignore invocations
with no message at all (these are meant to be quick sanity checks and are harmless) and where the
message is not a literal string (e.g. concatenations, calls to `String#format` or lambdas, which
are all considered custom messages not to be messed with).

Outside constructors, though, the checks are slightly relaxed. The rationale for this is that in
constructors `requireNonNull` is used primarily to sanity check the parameters and (usually) assign
them to respective fields, so there's a lot of regularity which can be exploited for checking
correctness. Outside of constructors, though, these calls are much more free-form and used for more
purposes. Thus, the check will not report cases where the message can't be recognized as an
"X is null" pattern, or if the part before that pattern is not an identifier. So, the following
cases are not allowed in constructors, but are allowed otherwise:

* `requireNonNull(parameter, "Please provide the information we require")`:
* `requireNonNull(parameter, "123 is null")`:

## Usage

Individual warnings can be suppressed with the `@SuppressWarnings("<CheckerName>")` annotation. For
the `DeprecatedApi` checker, `@SuppressWarnings({"deprecation", "DeprecatedApi"})` suppresses both
compiler and error-prone checker warnings. This is necessary as `DeprecatedApi` can be more
restrictive than the compiler deprecation check.

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
                <version>3</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```
