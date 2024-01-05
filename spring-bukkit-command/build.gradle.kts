plugins {
    id("spring-bukkit.shared")
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)

    api(projects.springBukkitCore)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}