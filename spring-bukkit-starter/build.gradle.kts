plugins {
    kotlin("jvm")
    id("spring-bukkit.publish")
}

dependencies {
    api(projects.springBukkitCore)
    api(projects.springBukkitCommand)
    api(projects.springBukkitView)
    runtimeOnly(projects.springBukkitSupport)

    api(libs.jakarta.annotation)
    api(libs.spring.tx)
}