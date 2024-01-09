plugins {
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)

    api(libs.kotlin.reflect)
    api(libs.spring.boot.autoconfigure)
    api(libs.spring.aspects)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}