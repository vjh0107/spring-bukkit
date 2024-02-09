dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.folia)

    compileOnly(projects.view)
    compileOnly(projects.command)
    compileOnly(projects.kotlinxCoroutines)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}