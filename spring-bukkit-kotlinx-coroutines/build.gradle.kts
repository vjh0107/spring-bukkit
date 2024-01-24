dependencies {
    compileOnly(libs.spigot)
    api(libs.kotlinx.coroutines.core)

    api(projects.core)
    implementation(projects.command)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}