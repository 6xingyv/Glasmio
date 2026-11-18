plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(project(":glasmio-core"))
    implementation(project(":glasmio-symbols"))
    implementation(project(":glasmio-motion"))
    implementation(project(":glasmio-optics"))
}

compose.desktop {
    application {
        mainClass = "io.github.neoruaa.glasmio.desktop.MainKt"
    }
}
