plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.graph.generator)
    `maven-publish`
    signing
}

val subProjectPlugins = listOf(
    libs.plugins.kotlin.jvm,
    libs.plugins.kotlin.plugin.spring,
    libs.plugins.spring.boot,
)

subprojects {
    group = extra["project.group"].toString()
    version = extra["project.version"].toString()

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

dependencyGraphGenerator {
    projectGenerators.configureEach {
        includeProject = { project ->
            project.name != "starter"
        }
        projectNode = { node, _ ->
            node.add(guru.nidi.graphviz.attribute.Style.SOLID, guru.nidi.graphviz.attribute.Color.rgb("#000000"))
        }
    }
}

group = extra["project.group"].toString()
version = extra["project.version"].toString()

if (extra.properties.keys.any { it.startsWith("signing.") } || System.getenv().keys.any { it.startsWith("SIGNING_") }) {
    subprojects {
        setupPublication(this)
    }
}

fun Project.setupPublication(project: Project) {
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "org.gradle.signing")

    tasks.withType<Jar> {
        archiveClassifier.set("")
    }

    extensions.getByType<JavaPluginExtension>().run {
        withSourcesJar()
        withJavadocJar()
    }

    publishing {
        publications {
            create<MavenPublication>("mavenCentral") {
                this.groupId = extra["project.group"]!!.toString()
                this.version = extra["project.version"]!!.toString()
                this.artifactId = "${project.rootProject.name}-${project.name.lowercase()}"
                from(components["java"])

                pom {
                    name.set(project.rootProject.name)
                    url.set(extra["project.url"]!!.toString())
                    description.set(extra["project.description"]!!.toString())

                    licenses {
                        license {
                            name.set(extra["project.license"]!!.toString())
                            url.set(extra["project.license.url"]!!.toString())
                        }
                    }
                    developers {
                        developer {
                            id.set(extra["project.developer.id"]!!.toString())
                            name.set(extra["project.developer.name"]!!.toString())
                            email.set(extra["project.developer.email"]!!.toString())
                        }
                        scm {
                            url.set(extra["project.url.scm"]!!.toString())
                        }
                    }
                }

                repositories {
                    maven {
                        name = "sonatype"
                        setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                        credentials {
                            username = System.getenv("OSSRH_USERNAME")
                            password = System.getenv("OSSRH_PASSWORD")
                        }
                    }
                }

                signing {
                    setRequired {
                        !project.version.toString()
                            .contains("-SNAPSHOT") && gradle.taskGraph.allTasks.any { it is PublishToMavenRepository }
                    }
                    if (!extra.has("signing.keyId")) {
                        extra["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
                    }
                    if (!extra.has("signing.password")) {
                        extra["signing.password"] = System.getenv("SIGNING_PASSPHRASE")
                    }
                    if (!extra.has("signing.secretKeyRingFile")) {
                        extra["signing.secretKeyRingFile"] = if (System.getenv("SIGNING_SECRET_KEY_RING_FILE_ABSOLUTE") != null) {
                            System.getenv("SIGNING_SECRET_KEY_RING_FILE_ABSOLUTE")
                        } else {
                            System.getenv("HOME") + "/" + System.getenv("SIGNING_SECRET_KEY_RING_FILE")
                        }
                    }
                    sign(this@publications)
                }
            }
        }
    }

    tasks.withType<AbstractPublishToMaven>().configureEach {
        val signingTasks = tasks.withType<Sign>()
        mustRunAfter(signingTasks)
    }
}