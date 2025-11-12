package ru.wtapp.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.wtapp.models.AuthRequest
import ru.wtapp.models.RegisterRequest
import ru.wtapp.services.AuthService

fun Route.authRoutes() {
    val authService = AuthService()

    route("/auth") {
        post("/register") {
            try {
                val request = call.receive<RegisterRequest>()

                // Валидация
                if (request.username.length < 3) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Username must be at least 3 characters"))
                    return@post
                }
                if (request.password.length < 6) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Password must be at least 6 characters"))
                    return@post
                }

                val response = authService.register(request)
                call.respond(response)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Registration failed"))
            }
        }

        post("/login") {
            try {
                val request = call.receive<AuthRequest>()
                val response = authService.login(request)
                call.respond(response)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Login failed"))
            }
        }
    }
}