package ru.wtapp.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import ru.wtapp.models.UserResponse
import ru.wtapp.services.UserService

fun Route.userRoutes() {
    val userService = UserService()

    route("/users") {
        authenticate("auth-jwt") {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val userId = UUID.fromString(principal?.getClaim("userId", String::class))

                val user = userService.getUserById(userId)
                if (user != null) {
                    call.respond(user)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "User not found"))
                }
            }

            get("/search") {
                val query = call.request.queryParameters["query"] ?: ""
                if (query.length < 2) {
                    call.respond(emptyList<UserResponse>())
                    return@get
                }

                val users = userService.searchUsers(query)
                call.respond(users)
            }
        }
    }
}