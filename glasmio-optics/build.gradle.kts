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
        namespace = "io.github.neoruaa.glasmio.optics"
        compileSdk = 37
        minSdk = 33
        compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21) }
    }
    jvm("desktop")
    iosArm64()
    iosSimulatorArm64()
    jvmToolchain(21)

    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.ui)
        }
        commonTest.dependencies { implementation(kotlin("test")) }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("glasmio-optics")
            description.set("Platform-independent Liquid Glass optical transfer functions and shader source")
            url.set("https://github.com/NEORUAA/Glasmio")
            licenses {
                license {
                    name.set("Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("NEORUAA")
                    name.set("NEORUAA")
                    url.set("https://github.com/NEORUAA")
                }
            }
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
