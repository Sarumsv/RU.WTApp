package ru.wtapp

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    install(CORS) {
        anyHost()
        allowHeader("Content-Type")
        allowHeader("Authorization")
    }

    routing {
        get("/") {
            call.respond(
                mapOf(
                    "message" to "Car Chat Backend is running!",
                    "status" to "OK",
                    "version" to "1.0.0"
                )
            )
        }

        get("/health") {
            call.respond(mapOf("status" to "healthy"))
        }

        // Простой тестовый эндпоинт для проверки
        get("/test/users") {
            val users = listOf(
                mapOf("id" to "1", "username" to "test1", "displayName" to "Test User 1"),
                mapOf("id" to "2", "username" to "test2", "displayName" to "Test User 2")
            )
            call.respond(users)
        }
    }
}