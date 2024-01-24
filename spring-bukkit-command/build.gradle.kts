dependencies {
    compileOnly(libs.spigot)
    api(libs.jakarta.annotation)

    api(projects.core)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}