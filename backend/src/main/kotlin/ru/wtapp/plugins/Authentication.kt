package ru.wtapp.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

object JwtConfig {
    const val SECRET = "car-chat-secret-key-change-in-production"
    const val ISSUER = "car-chat-backend"
    const val AUDIENCE = "car-chat-users"

    val algorithm = Algorithm.HMAC256(SECRET)

    fun makeToken(userId: String, username: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .sign(algorithm)
    }
}

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT.require(JwtConfig.algorithm)
                    .withIssuer(JwtConfig.ISSUER)
                    .withAudience(JwtConfig.AUDIENCE)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}