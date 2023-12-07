plugins {
    `kotlin-dsl`
    `maven-publish`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.gradle.plugin-publish:com.gradle.plugin-publish.gradle.plugin:1.2.0")
}