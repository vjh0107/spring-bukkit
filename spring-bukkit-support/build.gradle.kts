dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.paper)
    implementation(libs.protocollib)

    api(projects.core)
    implementation(projects.command)
    implementation(projects.view)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}