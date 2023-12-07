plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("spring-bukkit.publish")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.paper)

    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.core)
    api(libs.spring.boot.autoconfigure)
    api(libs.spring.aspects)

    testImplementation(libs.spigot)
    testImplementation(libs.spring.test)
}

tasks.compileKotlin {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.named("bootJar") {
    enabled = false
}