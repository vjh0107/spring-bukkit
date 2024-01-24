dependencies {
    compileOnly(libs.spigot)

    api(projects.core)
    api(libs.spring.data.jpa)
    api(libs.hibernate.core)
    implementation(libs.hikaricp)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}