dependencies {
    api(projects.springBukkitStarters.starter)
    api(projects.jpa)

    implementation(libs.hikaricp)
}

projectGrapher {
    group = "starters"
}