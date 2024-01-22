plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.graph.generator)
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")

    afterEvaluate {
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