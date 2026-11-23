plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    android {
        namespace = "io.github.neoruaa.glasmio.backdrop"
        compileSdk = 37
        minSdk = 33
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
    jvm("desktop")
    iosArm64()
    iosSimulatorArm64()
    jvmToolchain(21)

    sourceSets {
        commonMain.dependencies {
            api(project(":glasmio-core"))
            api(project(":glasmio-optics"))
            api(compose.ui)
            api(compose.runtime)
            api(compose.foundation)
            // A genuine GPU-backed runtime-effect pipeline for Desktop/iOS.
            // The public 2.0.0 release is on Maven Central; no Material UI is used.
            api("com.mocharealm.gaze:glassy-liquid-effect:2.0.0")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
