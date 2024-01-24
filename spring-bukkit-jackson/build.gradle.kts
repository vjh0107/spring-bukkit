dependencies {
    compileOnly(libs.spigot)

    api(projects.core)

    api(libs.jackson.databind)
    api(libs.jackson.datatype.jdk8)
    api(libs.jackson.datatype.jsr310)
    api(libs.jackson.module.parameter.names)
    api(libs.jackson.module.kotlin)

    testImplementation(libs.paper)
    testImplementation(libs.spring.test)
    testImplementation(libs.mockbukkit)
}