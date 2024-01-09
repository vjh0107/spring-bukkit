plugins {
    kotlin("jvm")
    id("spring-bukkit.publish")
}

dependencies {
    api(projects.core)
    api(projects.command)
    api(projects.view)
    runtimeOnly(projects.support)

    api(libs.jakarta.annotation)
    api(libs.spring.tx)
}