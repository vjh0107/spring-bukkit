dependencies {
    compileOnly(libs.spigot)

    api(projects.core)

    api(libs.kotlinx.serialization.json)
    api(libs.kotlin.reflect)

    testImplementation(libs.paper)
    testImplementation(libs.spring.test)
    testImplementation(libs.mockbukkit)
}