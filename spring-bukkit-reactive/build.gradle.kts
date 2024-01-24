dependencies {
    compileOnly(libs.spigot)

    api(projects.core)
    api(libs.reactor)
    implementation(projects.view)

    testImplementation(libs.paper)
    testImplementation(libs.spring.test)
    testImplementation(libs.mockbukkit)
}