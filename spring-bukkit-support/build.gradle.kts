dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.folia)
    implementation(libs.protocollib)

    api(projects.core)
    implementation(projects.command)
    implementation(projects.view)
    implementation(projects.kotlinxCoroutines)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}