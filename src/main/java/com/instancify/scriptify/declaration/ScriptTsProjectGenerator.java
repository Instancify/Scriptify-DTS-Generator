package com.instancify.scriptify.declaration;

import com.instancify.scriptify.api.script.Script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Generator for TypeScript/JavaScript project with declarations.
 */
public class ScriptTsProjectGenerator {

    /**
     * Script header comment.
     */
    private static final String SCRIPT_HEADER_COMMENT = """
            /**
             * Write your code below
             */
            """;

    /**
     * Type Script config with types declarations support.
     */
    public static final String TYPE_SCRIPT_CONFIG = """
            {
                "compilerOptions": {
                    "checkJs": true,
                    "allowJs": true,
                    "typeRoots": ["./types"]
                }
            }
            """;

    private final ScriptTsDeclarationGenerator generator;

    public ScriptTsProjectGenerator(Script<?> script) {
        this.generator = new ScriptTsDeclarationGenerator(script);
    }

    /**
     * Generates TS projects with tsconfig with declarations support.
     *s
     * @param path Path of generated project
     */
    public void generate(Path path) {
        Path src = path.resolve("src");

        if (!path.toFile().exists()) {
            path.toFile().mkdirs();
            if (!src.toFile().exists()) {
                src.toFile().mkdirs();
            }
        }

        String declaration = generator.generate();
        try {
            Files.writeString(src.resolve("types.d.ts"), declaration);
            Files.writeString(src.resolve("script.js"), SCRIPT_HEADER_COMMENT);
            Files.writeString(path.resolve("tsconfig.json"), TYPE_SCRIPT_CONFIG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
