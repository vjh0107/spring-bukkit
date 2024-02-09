dependencies {
    compileOnly(libs.spigot)

    api(projects.core)

    testImplementation(libs.paper)
    testImplementation(libs.spring.test)
    testImplementation(libs.mockbukkit)
}

projectGrapher {
    group = "serialization"
}