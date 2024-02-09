dependencies {
    compileOnly(libs.spigot)
    api(libs.kotlinx.coroutines.core)

    api(projects.core)
    compileOnly(projects.command)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}

projectGrapher {
    group = "reactive"
}