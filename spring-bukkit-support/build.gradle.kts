plugins {
    id("spring-bukkit.shared")
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.paper)

    api(project(":spring-bukkit-core"))
    api(libs.kotlin.reflect)
    api(libs.spring.boot.autoconfigure)
    api(libs.spring.aspects)
    api(libs.spring.tx)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}