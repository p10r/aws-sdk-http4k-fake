plugins {
    kotlin("jvm") version "1.9.0"
}

group = "de.p10r"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("software.amazon.awssdk:s3:2.20.140")
    implementation("org.http4k:http4k-connect-amazon-s3-fake:5.1.6.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}