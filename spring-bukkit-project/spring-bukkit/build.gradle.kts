dependencies {
    api(libs.spring.boot.starter) {
        exclude(module = "spring-boot-starter-logging")
    }
    compileOnly(libs.spigot)
}