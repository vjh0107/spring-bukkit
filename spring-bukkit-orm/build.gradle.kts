dependencies {
    compileOnly(libs.spigot)

    api(projects.core)
    api(projects.serialization)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}

projectGrapher {
    group = "orm"
}