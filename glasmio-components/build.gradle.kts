import org.gradle.api.publish.maven.MavenPublication

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("maven-publish")
    id("signing")
}

kotlin {
    android {
        namespace = "io.github.neoruaa.glasmio.components"
        compileSdk = 37
        minSdk = 33
        compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21) }
    }
    jvm("desktop")
    listOf(iosArm64(), iosSimulatorArm64(), iosX64()).forEach { target ->
        target.binaries.framework {
            baseName = "GlasmioComponents"
            isStatic = true
        }
    }
    jvmToolchain(21)

    sourceSets {
        commonMain.dependencies {
            api(project(":glasmio-core"))
            api(compose.runtime)
            api(compose.foundation)
            api(compose.ui)
            api(compose.material3)
        }
        androidMain.dependencies {
            implementation(project(":glasmio-icons"))
        }
        desktopMain.dependencies {
            implementation(compose.materialIconsExtended)
        }
        commonTest.dependencies { implementation(kotlin("test")) }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("glasmio-components")
            description.set("Shared Compose Multiplatform controls and shape drawing")
            url.set("https://github.com/NEORUAA/Glasmio")
            licenses { license { name.set("Apache License, Version 2.0"); url.set("https://www.apache.org/licenses/LICENSE-2.0.txt") } }
            developers { developer { id.set("NEORUAA"); name.set("NEORUAA"); url.set("https://github.com/NEORUAA") } }
            scm {
                url.set("https://github.com/NEORUAA/Glasmio")
                connection.set("scm:git:https://github.com/NEORUAA/Glasmio.git")
                developerConnection.set("scm:git:ssh://git@github.com/NEORUAA/Glasmio.git")
            }
        }
    }
    repositories {
        maven {
            name = "staging"
            url = rootProject.layout.buildDirectory.dir("maven").get().asFile.toURI()
        }
    }
}

signing {
    val key = providers.environmentVariable("MAVEN_SIGNING_KEY").orNull
    if (key != null) {
        useInMemoryPgpKeys(key, providers.environmentVariable("MAVEN_SIGNING_PASSWORD").orNull)
        sign(publishing.publications)
    }
}
