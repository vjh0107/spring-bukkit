rootProject.name = "spring-bukkit"

includeBuild("build-logic")

projects(
    "command",
    "core",
    "jpa",
    "coroutines",
    "starter",
    "support",
    "view"
)

fun projects(vararg projects: String) {
    projects.forEach { project ->
        include(":spring-bukkit-$project")
        project(":spring-bukkit-$project").name = project
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    versionCatalogs {
        create("libs") {
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").version(extra["kotlin.version"].toString())
            plugin("kotlin-plugin-spring", "org.jetbrains.kotlin.plugin.spring").version(extra["kotlin.version"].toString())
            plugin("spring-boot", "org.springframework.boot").version(extra["spring.boot.version"].toString())

            library("spigot", "org.spigotmc:spigot-api:${extra["spigot.version"]}")
            library("paper", "io.papermc.paper:paper-api:${extra["spigot.version"]}")

            library("kotlin-stdlib", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${extra["kotlin.version"]}")
            library("kotlin-reflect", "org.jetbrains.kotlin:kotlin-reflect:${extra["kotlin.version"]}")
            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx:kotlinx-coroutines-core:${extra["kotlinx.coroutines.version"]}")

            library("spring-boot-autoconfigure", "org.springframework.boot:spring-boot-autoconfigure:${extra["spring.boot.version"]}")
            library("spring-aspects", "org.springframework:spring-aspects:${extra["spring.version"]}")
            library("spring-tx", "org.springframework:spring-tx:${extra["spring.version"]}")
            library("jakarta-annotation", "jakarta.annotation:jakarta.annotation-api:${extra["jakarta.annotation.version"]}")

            library("spring-data-jpa", "org.springframework.data:spring-data-jpa:${extra["spring.data.jpa.version"]}")
            library("hibernate-core", "org.hibernate.orm:hibernate-core:${extra["hibernate.version"]}")
            library("hikaricp", "com.zaxxer:HikariCP:${extra["hikaricp.version"]}")

            library("spring-test", "org.springframework.boot:spring-boot-starter-test:${extra["spring.boot.version"]}")
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")