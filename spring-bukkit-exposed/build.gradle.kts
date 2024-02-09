dependencies {
    compileOnly(libs.spigot)

    api(projects.orm)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}

projectGrapher {
    group = "orm"
}