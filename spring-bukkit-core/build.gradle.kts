dependencies {
    compileOnly(libs.spigot)

    api(libs.kotlin.reflect)
    api(libs.spring.boot.autoconfigure)
    api(libs.spring.aspects)
    api(libs.jakarta.annotation)
    api(libs.spring.tx)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}