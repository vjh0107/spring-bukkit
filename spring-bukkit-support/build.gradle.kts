plugins {
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.paper)

    api(projects.core)
    implementation(projects.command)
    implementation(projects.view)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}