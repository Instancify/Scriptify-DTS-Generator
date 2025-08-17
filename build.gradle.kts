plugins {
    `java-library`
    `maven-publish`
    id("java")
}

group = "com.instancify.scriptify.declaration"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
repositories {
    mavenCentral()
    maven("https://repo.instancify.app/snapshots")
}

dependencies {
    compileOnlyApi("com.instancify.scriptify:api:1.4.2-SNAPSHOT")
    implementation("com.instancify.scriptify:script-js-graalvm:1.4.2-SNAPSHOT")
    implementation("com.instancify.scriptify:common:1.4.2-SNAPSHOT")
    compileOnlyApi("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "instancify"
            url = uri("https://repo.instancify.app/snapshots")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}