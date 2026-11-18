import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension

plugins {
    id("com.android.library") version "9.3.1" apply false
    id("com.android.application") version "9.3.1" apply false
    id("com.android.kotlin.multiplatform.library") version "9.3.1" apply false
    id("org.jetbrains.kotlin.multiplatform") version "2.4.21" apply false
    id("org.jetbrains.kotlin.jvm") version "2.4.21" apply false
    id("org.jetbrains.compose") version "1.12.2" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.21" apply false
}
abstract class PrepareLegalResources : DefaultTask() {
    @get:Input abstract val moduleName: Property<String>
    @get:InputFiles @get:PathSensitive(PathSensitivity.NONE)
    abstract val legalFiles: ConfigurableFileCollection
    @get:OutputDirectory abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun prepare() {
        val output = outputDirectory.get().asFile
        output.deleteRecursively()
        val target = output.resolve("META-INF/glasmio/${moduleName.get()}")
        target.mkdirs()
        legalFiles.forEach { source ->
            val destination = if (source.extension == "txt") target.resolve("licenses/${source.name}")
                else target.resolve(source.name)
            destination.parentFile.mkdirs()
            source.copyTo(destination, overwrite = true)
        }
    }
}

allprojects {
    group = providers.gradleProperty("GROUP").get()
    version = providers.gradleProperty("VERSION_NAME").get()
}

subprojects {
    plugins.withId("com.android.library") {
        apply(plugin = "maven-publish")
        apply(plugin = "signing")
        val legalResources = tasks.register<PrepareLegalResources>("prepareLegalResources") {
            moduleName.set(project.name)
            legalFiles.from(rootProject.file("LICENSE"), rootProject.file("THIRD_PARTY_NOTICES.md"), rootProject.file("licenses/Apache-2.0.txt"))
            outputDirectory.set(layout.buildDirectory.dir("generated/legal-resources"))
        }
        configure<LibraryAndroidComponentsExtension> {
            onVariants(selector().all()) { variant ->
                variant.sources.resources?.addGeneratedSourceDirectory(
                    legalResources, PrepareLegalResources::outputDirectory,
                )
            }
        }
        tasks.withType<Jar>().configureEach {
            if (name == "sourceReleaseJar") {
                from(legalResources)
                if (project.name == "glasmio-glass") {
                    from("src/main/cpp") { into("native") }
                }
            }
        }
        configure<LibraryExtension> {
            compileSdk = 37
            defaultConfig { minSdk = 33; testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }
            buildFeatures { compose = true }
            publishing { singleVariant("release") { withSourcesJar(); withJavadocJar() } }
            lint { abortOnError = true; checkReleaseBuilds = true }
        }
        afterEvaluate {
            configure<PublishingExtension> {
                publications {
                    create<MavenPublication>("release") {
                        from(components["release"])
                        pom {
                            name.set(project.name)
                            description.set("iOS-inspired Jetpack Compose components and Liquid Glass rendering")
                            url.set("https://github.com/NEORUAA/Glasmio")
                            licenses { license { name.set("Apache License, Version 2.0"); url.set("https://www.apache.org/licenses/LICENSE-2.0.txt") } }
                            developers { developer { id.set("NEORUAA"); name.set("NEORUAA"); url.set("https://github.com/NEORUAA") } }
                            scm { url.set("https://github.com/NEORUAA/Glasmio"); connection.set("scm:git:https://github.com/NEORUAA/Glasmio.git"); developerConnection.set("scm:git:ssh://git@github.com/NEORUAA/Glasmio.git") }
                        }
                    }
                }
                repositories { maven { name = "staging"; url = rootProject.layout.buildDirectory.dir("maven").get().asFile.toURI() } }
            }
            configure<SigningExtension> {
                val key = providers.environmentVariable("MAVEN_SIGNING_KEY").orNull
                if (key != null) {
                    useInMemoryPgpKeys(key, providers.environmentVariable("MAVEN_SIGNING_PASSWORD").orNull)
                    sign(project.extensions.getByType<PublishingExtension>().publications)
                }
            }
        }

    }
}


// Fail even if a third-party dependency accidentally reintroduces Material at
// compilation or runtime. Direct source imports are checked separately in CI.
tasks.register("verifyNoMaterialDependencies") {
    group = "verification"
    description = "Verify Android and Desktop compile/runtime graphs contain no Material Design artifacts"
    doLast {
        val forbidden = mutableListOf<String>()
        allprojects.forEach { dependencyProject ->
            dependencyProject.configurations.filter { configuration ->
                configuration.isCanBeResolved &&
                    configuration.name.endsWith("Classpath", ignoreCase = true) &&
                    listOf("debug", "release", "android", "desktop").any { prefix ->
                        configuration.name.startsWith(prefix, ignoreCase = true)
                    }
            }.forEach { configuration ->
                configuration.incoming.resolutionResult.allComponents.forEach { component ->
                    val module = component.moduleVersion ?: return@forEach
                    if (module.group == "androidx.compose.material" ||
                        module.group == "androidx.compose.material3" ||
                        module.group.startsWith("org.jetbrains.compose.material")
                    ) {
                        forbidden += "${dependencyProject.path}:${configuration.name}: ${module.group}:${module.name}:${module.version}"
                    }
                }
            }
        }
        check(forbidden.isEmpty()) {
            "Material Design dependencies are prohibited:\n" + forbidden.distinct().sorted().joinToString("\n")
        }
    }
}
