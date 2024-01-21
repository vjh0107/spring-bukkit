plugins {
    `maven-publish`
    signing
}

group = extra["project.group"].toString()
version = extra["project.version"].toString()

if (extra.properties.keys.any { it.startsWith("signing.") } || System.getenv().keys.any { it.startsWith("SIGNING_") }) {
    setupPublication()
}

fun setupPublication() {
    val group = project.extra["project.group"]!!.toString()
    val version = project.extra["project.version"]!!.toString()
    val projectUrl = extra["project.url"]!!.toString()
    val projectScmUrl = extra["project.url.scm"]!!.toString()
    val projectDescription = extra["project.description"]!!.toString()
    val projectLicense = extra["project.license"]!!.toString()
    val projectLicenseUrl = extra["project.license.url"]!!.toString()
    val projectDeveloperId = extra["project.developer.id"]!!.toString()
    val projectDeveloperName = extra["project.developer.name"]!!.toString()
    val projectDeveloperEmail = extra["project.developer.email"]!!.toString()

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
                this.groupId = group
                this.version = version
                this.artifactId = "${project.rootProject.name}-${project.name.lowercase()}"
                from(components["java"])

                pom {
                    name.set(project.rootProject.name)
                    url.set(projectUrl)
                    description.set(projectDescription)

                    licenses {
                        license {
                            name.set(projectLicense)
                            url.set(projectLicenseUrl)
                        }
                    }
                    developers {
                        developer {
                            id.set(projectDeveloperId)
                            name.set(projectDeveloperName)
                            email.set(projectDeveloperEmail)
                        }
                        scm {
                            url.set(projectScmUrl)
                        }
                    }
                }

                repositories {
                    maven {
                        name = "sonatype"
                        setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                        credentials {
                            username = if (extra.has("ossrh.username")) {
                                extra["ossrh.username"].toString()
                            } else {
                                System.getenv("OSSRH_USERNAME")
                            }

                            password = if (extra.has("ossrh.password")) {
                                extra["ossrh.password"].toString()
                            } else {
                                System.getenv("OSSRH_PASSWORD")
                            }
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
                            ?: throw IllegalStateException("SIGNING_KEY_ID not set.")
                    }
                    if (!extra.has("signing.password")) {
                        extra["signing.password"] = System.getenv("SIGNING_PASSPHRASE")
                            ?: throw IllegalStateException("SIGNING_PASSPHRASE not set.")
                    }
                    if (!extra.has("signing.secretKeyRingFile")) {
                        extra["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")?.let {
                            System.getenv("HOME") + "/" + it
                        } ?: throw IllegalStateException("SIGNING_SECRET_KEY_RING_FILE not set.")
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