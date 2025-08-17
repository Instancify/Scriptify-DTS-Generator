# Scriptify DTS Generator
Generator for TypeScript declarations Scriptify functions and constants

## Example
Generate functions and constant declarations
```java
ScriptTsDeclarationGenerator generator = new ScriptTsDeclarationGenerator(script);
// Generated declaration string
String declaration = generator.generate();
// Save generated declaration
generator.save(Path.of("types.d.ts"));
```
Generate TypeScript (with JavaScript support) project with declarations
```java
ScriptTsProjectGenerator generator = new ScriptTsProjectGenerator(script);
generator.generate(Path.of("code"));
```

## Maven
Adding repo:
```xml
<repositories>
    <repository>
        <id>instancify-repository-snapshots</id>
        <url>https://repo.instancify.app/snapshots</url>
    </repository>
</repositories>
```

Adding dependency:
```xml
<dependency>
    <groupId>com.instancify.scriptify.declaration</groupId>
    <artifactId>generator</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Gradle
Adding repo:
```groovy
maven {
    name "instancifyRepositorySnapshots"
    url "https://repo.instancify.app/snapshots"
}
```

Adding dependency:
```groovy
implementation "com.instancify.scriptify.declaration:generator:1.0.0-SNAPSHOT"
```