plugins {
    id("spring-bukkit.shared")
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)

    api(libs.spring.boot.autoconfigure)
    api(libs.spring.data.jpa)
    api(libs.hibernate.core)
    api(libs.jakarta.persistence.api)
    api(libs.jakarta.transaction.api)
    implementation(libs.hikaricp)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}