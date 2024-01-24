plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.graph.generator)
}

val subProjectPlugins = listOf(
    libs.plugins.kotlin.jvm,
    libs.plugins.kotlin.plugin.spring,
    libs.plugins.spring.boot
)

subprojects {
    subProjectPlugins.forEach {
        pluginManager.apply(it.get().pluginId)
    }

    afterEvaluate {
        tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar>().configureEach {
            this.enabled = false
        }
        tasks.withType<Jar>().configureEach {
            destinationDirectory.get().asFile.listFiles()?.forEach {
                it.delete()
            }
            archiveBaseName.set("${rootProject.name}-${project.name}")
        }
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            kotlinOptions {
                freeCompilerArgs += "-Xjsr305=strict"
                jvmTarget = "17"
            }
        }
        extensions.getByType<JavaPluginExtension>().sourceCompatibility = JavaVersion.VERSION_17
    }
}