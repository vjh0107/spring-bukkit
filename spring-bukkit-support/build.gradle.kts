plugins {
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.paper)

    api(projects.core)
    implementation(projects.command)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}