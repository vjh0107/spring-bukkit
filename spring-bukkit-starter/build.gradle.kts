dependencies {
    api(projects.core)
    api(projects.command)
    api(projects.view)
    implementation(projects.support)

    api(libs.jakarta.annotation)
    api(libs.spring.tx)
}