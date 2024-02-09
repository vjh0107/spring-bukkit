dependencies {
    api(projects.springBukkitStarters.starter)
    api(projects.exposed)

    implementation(libs.hikaricp)
}

projectGrapher {
    group = "starters"
}