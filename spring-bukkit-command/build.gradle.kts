plugins {
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)

    api(projects.core)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}