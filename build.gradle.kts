subprojects {
    group = extra["project.group"].toString()
    version = extra["project.version"].toString()

    with(pluginManager) {
        apply(JavaLibraryPlugin::class)
        apply(MavenPublishPlugin::class)
    }

    extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                groupId = project.group.toString()
                version = project.version.toString()
                artifactId = project.name
                from(components["java"])
            }
        }
    }

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
    }
}