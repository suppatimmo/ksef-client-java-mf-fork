# Build Artifacts

This directory contains the pre-built JAR files from the Maven build.

## Files

- **ksef-client-0.0.0-SNAPSHOT.jar** (423 KB) - Main library JAR containing compiled classes
- **ksef-client-0.0.0-SNAPSHOT-sources.jar** (229 KB) - Source code JAR for IDE integration
- **ksef-client-0.0.0-SNAPSHOT-javadoc.jar** (2.3 MB) - Javadoc documentation JAR

## Usage

To use the main library JAR in your project, you can:

1. Add it to your project's classpath
2. Include it as a dependency in your build tool
3. Deploy it to your local or organizational Maven repository

## Building from Source

To rebuild these artifacts yourself:

```bash
# Using Maven
mvn clean package -DskipTests

# Using Gradle
./gradlew build
```

The Maven build will create these files in `ksef-client/target/`.
