plugins {
    kotlin("jvm")
    id("spring-bukkit.publish")
}

dependencies {
    api(projects.springBukkitCore)
    api(projects.springBukkitCommand)
    runtimeOnly(projects.springBukkitSupport)
}