import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "9.2.2"
    id("java")
    `java-library`
}

group = "com.github.grassproject.folra"
version = "${rootProject.properties["project_version"]}"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.nekroplex.com/releases")
    maven("https://repo.nexomc.com/releases")
    maven("https://maven.devs.beer/")
    maven("https://repo.momirealms.net/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${rootProject.properties["paper_version"]}-R0.1-SNAPSHOT")

    compileOnly("gg.aquatic:AEAPI:1.0")
    compileOnly("com.nexomc:nexo:${rootProject.properties["nexo_version"]}") { exclude("*") }
    compileOnly("dev.lone:api-itemsadder:${rootProject.properties["itemsadder_version"]}")
    compileOnly("net.momirealms:craft-engine-core:${rootProject.properties["craftengine_version"]}")
    compileOnly("net.momirealms:craft-engine-bukkit:${rootProject.properties["craftengine_version"]}")
    compileOnly("io.netty:netty-all:4.2.9.Final")

    implementation(project(":api"))
    implementation(project(":nms:v1_21_8"))
}

kotlin {
    jvmToolchain(21)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

val shadowJarPlugin = tasks.register<ShadowJar>("shadowJarPlugin") {
    archiveFileName.set("Folra-${project.version}.jar")

    destinationDirectory.set(file("${project.rootDir}/target"))

    from(sourceSets.main.get().output)
    configurations = listOf(project.configurations.runtimeClasspath.get())

    relocate("com.zaxxer.hikari", "com.github.grassproject.folracore.shadow.hikari")

//    exclude("kotlin/**", "kotlinx/**")
//    exclude("org/intellij/**")
//    exclude("org/jetbrains/**")
//    exclude("org/slf4j/**")
}

tasks.named("build") {
    // dependsOn(tasks.clean)
    dependsOn(shadowJarPlugin)
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}