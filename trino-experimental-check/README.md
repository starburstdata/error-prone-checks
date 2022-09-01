# Trino Experimental SPI Usage Checker

Bug checker for detecting usages of Trino SPIs that are annotated with the `@Experimental` 
annotation. Such SPIs should be avoided in libraries and shared components that other 
projects depend on. This checker is intended to be used by such libraries and shared 
components to ensure that they don't use them.

## Usage

To use the checker configure your project to build with the Error Prone Java compiler and add
`trino-experimental-check` annotation processor.

Default severity for the diagnostics reported by this checker is warning. To customize that pass
an additional compiler argument: `-Xep:UsingTrinoExperimentalSpi:ERROR`. Allowed values are: 
`ERROR`, `WARNING`, `SUGGESTION`, `OFF`.

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
                <groupId>com.starburstdata.errorprone</groupId>
                <artifactId>trino-experimental-check</artifactId>
                <version>1.0</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```