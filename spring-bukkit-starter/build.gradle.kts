plugins {
    kotlin("jvm")
    id("spring-bukkit.publish")
}

dependencies {
    api(projects.springBukkitCore)
    api(projects.springBukkitCommand)
    api(projects.springBukkitView)

    api(libs.jakarta.annotation)

    runtimeOnly(projects.springBukkitSupport)
}