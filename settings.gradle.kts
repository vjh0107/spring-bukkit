rootProject.name = "spring-bukkit"

projects(
    "command",
    "core",
    "jackson",
    "jpa",
    "kotlinx-coroutines",
    "kotlinx-serialization",
    "starter",
    "support",
    "view",
    "reactive"
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
            plugin("graph-generator", "com.vanniktech.dependency.graph.generator").version(extra["graph.generator.version"].toString())

            library("spigot", "org.spigotmc:spigot-api:${extra["spigot.version"]}")
            library("paper", "io.papermc.paper:paper-api:${extra["spigot.version"]}")
            library("folia", "dev.folia:folia-api:${extra["spigot.version"]}")
            library("mockbukkit", "com.github.seeseemelk:MockBukkit-v1.20:${extra["mock.bukkit.version"]}")

            library("kotlin-stdlib", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${extra["kotlin.version"]}")
            library("kotlin-reflect", "org.jetbrains.kotlin:kotlin-reflect:${extra["kotlin.version"]}")
            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx:kotlinx-coroutines-core:${extra["kotlinx.coroutines.version"]}")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:${extra["kotlinx.serialization.version"]}")

            library("spring-boot-autoconfigure", "org.springframework.boot:spring-boot-autoconfigure:${extra["spring.boot.version"]}")
            library("spring-aspects", "org.springframework:spring-aspects:${extra["spring.version"]}")
            library("spring-tx", "org.springframework:spring-tx:${extra["spring.version"]}")
            library("spring-test", "org.springframework.boot:spring-boot-starter-test:${extra["spring.boot.version"]}")
            library("jakarta-annotation", "jakarta.annotation:jakarta.annotation-api:${extra["jakarta.annotation.version"]}")

            library("spring-data-jpa", "org.springframework.data:spring-data-jpa:${extra["spring.data.jpa.version"]}")
            library("hibernate-core", "org.hibernate.orm:hibernate-core:${extra["hibernate.version"]}")
            library("hikaricp", "com.zaxxer:HikariCP:${extra["hikaricp.version"]}")

            library("jackson-databind", "com.fasterxml.jackson.core:jackson-databind:${extra["jackson.version"]}")
            library("jackson-datatype-jdk8", "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${extra["jackson.version"]}")
            library("jackson-datatype-jsr310", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${extra["jackson.version"]}")
            library("jackson-module-parameter-names", "com.fasterxml.jackson.module:jackson-module-parameter-names:${extra["jackson.version"]}")
            library("jackson-module-kotlin", "com.fasterxml.jackson.module:jackson-module-kotlin:${extra["jackson.version"]}")

            library("reactor", "io.projectreactor:reactor-core:${extra["reactor.version"]}")
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")