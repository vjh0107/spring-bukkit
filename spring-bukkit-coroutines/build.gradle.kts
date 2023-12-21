plugins {
    id("spring-bukkit.shared")
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)
    implementation(project(":spring-bukkit-core"))

    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.core)
    api(libs.spring.boot.autoconfigure)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}