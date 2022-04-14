plugins {
    id("fabric-loom") version "0.10-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower-mini") version "1.2.1"
    id("org.cadixdev.licenser") version "0.6.1"
}

val modVersion: String by project

group = "io.github.jamalam360"
version = modVersion

fun DependencyHandlerScope.includeModApi(dependency: Any) {
    modApi(dependency)
    include(dependency)
}

fun DependencyHandlerScope.includeModImplementation(dependency: Any) {
    modImplementation(dependency)
    include(dependency)
}

fun Task.input(q: String): String {
        return System.console().readLine("> $q: ")
}

repositories {
    val mavenUrls = listOf(
            "https://maven.terraformersmc.com/releases"
    )

    for (url in mavenUrls) {
        maven(url = url)
    }
}

dependencies {
    val minecraftVersion: String by project
    val mappingsVersion: String by project
    val loaderVersion: String by project
    val fabricApiVersion: String by project
    val reachEntityAttributesVersion: String by project
    val itemModelFixVersion: String by project
    val cardinalComponentsVersion: String by project
    val modMenuVersion: String by project

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$mappingsVersion:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    modRuntimeOnly("com.terraformersmc:modmenu:$modMenuVersion")
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
        archiveBaseName.set("LPA3227-Origins-Gear")
    }

    remapJar {
        archiveBaseName.set("LPA3227-Origins-Gear")
    }

    withType<JavaCompile> {
        options.release.set(17)
    }

    register("template") {
        doFirst {
            val gradleProperties = File("gradle.properties")
            val readme = File("README.md")
            val mainClass = File("src/main/java/io/github/jamalam360/TemplateModInit.java")
            val modJson = File("src/main/resources/fabric.mod.json")
            val mixinJson = File("src/main/resources/templatemod.mixins.json")
            val assetsDirectory = File("src/main/resources/assets/templatemod")

            val modId = input("Enter Mod ID")
            val modName = input("Enter Mod Name")
            val modDescription = input("Enter Mod Description")
            val useCustomGitHub = input("Use Custom GitHub Organization? (y/n)").toLowerCase() == "y"
            val githubOrg = if (useCustomGitHub) input("Enter GitHub Organization Name") else "JamCoreModding"
            val github = input("Enter GitHub Repository Name")

            val mainClassName = modName + "ModInit"

            val resources = "src/main/resources/"
            val java = "src/main/java/io/github/jamalam360/" + modId + "/"

            gradleProperties.writeText(gradleProperties.readText().replace("%id%", modId))
            readme.writeText(readme.readText().replace("%description%", modDescription))
            mainClass.writeText(
                mainClass.readText().replace("%id%", modId).replace("%name%", modName).replace("%mainclass%", mainClassName))
            modJson.writeText(
                modJson.readText().replace("%id%", modId).replace("%name%", modName).replace("%description%", modDescription)
                    .replace("%github%", github).replace("%githuborg%", githubOrg))

            mainClass.renameTo(File(java + mainClassName + ".java"))
            mixinJson.renameTo(File(resources + modId + ".mixins.json"))
            assetsDirectory.renameTo(File(resources + "assets/" + modId))
        }
    }
}


