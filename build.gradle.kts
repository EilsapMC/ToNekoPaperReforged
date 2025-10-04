import xyz.jpenilla.resourcefactory.bukkit.Permission

plugins {
    kotlin("jvm") version "2.2.0"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
    id("xyz.jpenilla.run-paper") version "3.0.1"
}


group = "suki.mrhua269"
version = "0.72.2"
description = "ToNeko plugin reforged version"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.8")
    }
}

paperPluginYaml {
    // Defaults for name, version, and description are inherited from the Gradle project
    main = "suki.mrhua269.tnbr.ToNekoBukkitReforged"
    authors.addAll("MrHua269","CrystalNeko")
    // configure fields...
    apiVersion = "1.21.8"
    foliaSupported = true
    permissions {
        register("toneko.command.give") {
            default = Permission.Default.OP
        }
        register("toneko.command.set") {
            default = Permission.Default.OP
        }
    }
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
