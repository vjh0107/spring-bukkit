plugins {
    id("spring-bukkit.shared")
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)
    api(libs.kotlinx.coroutines.core)

    api(projects.springBukkitCore)
    implementation(projects.springBukkitCommand)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}