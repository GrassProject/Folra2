plugins {
    kotlin("jvm")
}

group = "com.github.grassproject.folra.api"
version = parent!!.version

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${rootProject.properties["paper_version"]}-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}