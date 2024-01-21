plugins {
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)

    api(projects.core)
    api(libs.reactor)

    testImplementation(libs.paper)
    testImplementation(libs.spring.test)
    testImplementation(libs.mockbukkit)
}