import java.util.*

plugins {
    id("maven-publish")
    id("fabric-loom") version "0.11-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.6.0"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.0.0"
    id("org.cadixdev.licenser") version "0.6.1"
}

val modVersion: String by project

group = "io.github.jamalam360"
version = modVersion

repositories {
    val mavenUrls = listOf<String>()

    for (url in mavenUrls) {
        maven(url = url)
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:1.18.2+build.22:v2"))
    })

    modImplementation(libs.loader)
    modImplementation(libs.fabric.api)
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version
                )
            )
        }
    }

    build {
        dependsOn("updateLicenses")
    }

    jar {
        archiveBaseName.set("MultiblockLib")
    }

    remapJar {
        archiveBaseName.set("MultiblockLib")
    }

    withType<JavaCompile> {
        options.release.set(17)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.jamalam360"
            artifactId = "multiblock-lib"
            version = modVersion
            from(components["java"])
        }
    }

    repositories {
        if (project.rootProject.file("local.properties").exists()) {
            val localProperties = Properties()
            localProperties.load(project.rootProject.file("local.properties").inputStream())

            maven {
                name = "JamalamMavenRelease"
                url = uri("https://maven.jamalam.tech/releases")
                credentials {
                    username = localProperties["MAVEN_USERNAME"] as String
                    password = localProperties["MAVEN_PASSWORD"] as String
                }
            }
        }
    }
}
