plugins {
    id("spring-bukkit.shared")
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)

    api(projects.springBukkitCore)
    implementation(projects.springBukkitCommand)

    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.core)
    api(libs.spring.boot.autoconfigure)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}