val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.4.32"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.32"
}

group = "de.deluxesoftware"
version = "0.0.1"
application {
    mainClass.set("de.deluxesoftware.ApplicationKt")
}

repositories {
    mavenCentral()
}

application {
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:7.5.0")

    implementation("mysql:mysql-connector-java:8.0.23")
    implementation("com.zaxxer:HikariCP:4.0.3")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "15"
    }
}
