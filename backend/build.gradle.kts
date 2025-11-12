import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    application
    kotlin("plugin.serialization") version "1.9.10"
}

group = "ru.wtapp"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Ktor Core
    implementation("io.ktor:ktor-server-core:2.3.5")
    implementation("io.ktor:ktor-server-netty:2.3.5")
    implementation("io.ktor:ktor-server-status-pages:2.3.5")

    // Content Negotiation
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.5")

    // Security & CORS
    implementation("io.ktor:ktor-server-cors:2.3.5")
    implementation("io.ktor:ktor-server-auth:2.3.5")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.5")

    // WebSockets
    implementation("io.ktor:ktor-server-websockets:2.3.5")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.44.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.44.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.44.0")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Password Hashing
    implementation("at.favre.lib:bcrypt:0.10.2")

    // JWT
    implementation("com.auth0:java-jwt:4.4.0")

    // Logging
    implementation("io.ktor:ktor-server-call-logging:2.3.5")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // Testing
    testImplementation("io.ktor:ktor-server-tests:2.3.5")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.10")
}

application {
    mainClass.set("ru.wtapp.ApplicationKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
}