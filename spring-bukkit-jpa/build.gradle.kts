dependencies {
    compileOnly(libs.spigot)

    api(projects.orm)

    api(libs.spring.data.jpa)
    api(libs.hibernate.core)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}

projectGrapher {
    group = "orm"
}