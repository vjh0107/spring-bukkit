plugins {
    `maven-publish`
    signing
}

group = extra["project.group"].toString()
version = extra["project.version"].toString()

if (extra.properties.keys.any { it.startsWith("signing.") }) {
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

    if (extra["signing.keyId"] == null) {
        extra["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    }
    if (extra["signing.password"] == null) {
        extra["signing.password"] = System.getenv("SIGNING_PASSPHRASE")
    }
    if (extra["signing.secretKeyRingFile"] == null) {
        extra["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    }

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
                            username = extra["ossrh.username"]?.toString() ?: System.getenv("OSSRH_USERNAME")
                            password = extra["ossrh.password"]?.toString() ?: System.getenv("OSSRH_PASSWORD")
                        }
                    }
                }

                signing {
                    setRequired {
                        !project.version.toString()
                            .contains("-SNAPSHOT") && gradle.taskGraph.allTasks.any { it is PublishToMavenRepository }
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