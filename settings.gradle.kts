rootProject.name = "spring-bukkit-build"

includeProjects(file("spring-bukkit-project"))

fun includeProjects(projectDir: File) {
    projectDir.listFiles()?.forEach { projectFile ->
        val projectName = ":${projectFile.name}"
        include(projectName)
        project(projectName).projectDir = projectFile
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    versionCatalogs {
        create("libs")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")