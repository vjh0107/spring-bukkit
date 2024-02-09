dependencies {
    compileOnly(libs.spigot)
    api(libs.reactor)

    api(projects.core)
    compileOnly(projects.view)

    testImplementation(libs.paper)
    testImplementation(libs.spring.test)
    testImplementation(libs.mockbukkit)
}

projectGrapher {
    group = "reactive"
}