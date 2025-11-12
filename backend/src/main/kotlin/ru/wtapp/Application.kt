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
import ru.wtapp.plugins.configureAuthentication
import ru.wtapp.plugins.configureDatabase
import ru.wtapp.plugins.configureWebSockets
import ru.wtapp.routes.authRoutes
import ru.wtapp.routes.userRoutes
import ru.wtapp.routes.websocketRoutes

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Инициализация базы данных
    configureDatabase()

    // Плагины
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(CORS) {
        anyHost()
        allowHeader("Content-Type")
        allowHeader("Authorization")
        allowHeader("Origin")
        allowMethod(io.ktor.http.HttpMethod.Options)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
        allowMethod(io.ktor.http.HttpMethod.Patch)
    }

    // Конфигурация аутентификации и WebSockets
    configureAuthentication()
    configureWebSockets()

    // Маршруты
    routing {
        get("/") {
            call.respond(mapOf(
                "message" to "Car Chat Backend is running!",
                "status" to "OK",
                "version" to "1.0.0"
            ))
        }

        get("/health") {
            call.respond(mapOf("status" to "healthy"))
        }

        // Подключаем модули маршрутов
        authRoutes()
        userRoutes()
        websocketRoutes()
    }
}