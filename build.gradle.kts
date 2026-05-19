plugins {
    id("java-library")
    id("xyz.jpenilla.run-velocity") version "3.0.2"
    id("com.gradleup.shadow") version "8.3.6"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    implementation("com.electronwill.night-config:toml:3.8.4")
    implementation("net.dv8tion:JDA:6.4.1") {
        exclude(module = "opus-java")
        exclude(module = "tink")
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    runVelocity {
        // Configure the Velocity version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        velocityVersion("3.5.0-SNAPSHOT")
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("velocity-plugin.json") {
            expand(props)
        }
    }

    jar {
        enabled = false
    }

    // Keep the shaded jar as the only artifact.
    shadowJar {
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}
