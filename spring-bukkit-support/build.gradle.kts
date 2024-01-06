plugins {
    id("spring-bukkit.shared")
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.paper)

    api(projects.springBukkitCore)
    api(projects.springBukkitCommand)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}