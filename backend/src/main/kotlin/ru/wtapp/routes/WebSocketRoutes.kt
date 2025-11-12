package ru.wtapp.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.wtapp.plugins.JwtConfig
import ru.wtapp.plugins.WebSocketManager
import java.util.*

fun Route.websocketRoutes() {
    webSocket("/ws") {
        val token = call.request.queryParameters["token"]

        // Валидация токена
        val userId = try {
            if (token == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Token required"))
                return@webSocket
            }

            val jwt = JWT.require(JwtConfig.algorithm)
                .withIssuer(JwtConfig.ISSUER)
                .withAudience(JwtConfig.AUDIENCE)
                .build()
                .verify(token)

            jwt.getClaim("userId").asString()
        } catch (e: Exception) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Invalid token"))
            return@webSocket
        }

        val username = try {
            val jwt = JWT.require(JwtConfig.algorithm)
                .withIssuer(JwtConfig.ISSUER)
                .withAudience(JwtConfig.AUDIENCE)
                .build()
                .verify(token)
            jwt.getClaim("username").asString()
        } catch (e: Exception) {
            "unknown"
        }

        val connectionId = UUID.randomUUID().toString()
        val connection = ru.wtapp.plugins.Connection(this)

        WebSocketManager.addConnection(connectionId, connection)

        try {
            // Уведомляем о подключении
            val connectMessage = Json.encodeToString(mapOf(
                "type" to "user_connected",
                "userId" to userId,
                "username" to username
            ))
            WebSocketManager.broadcast(connectMessage, connectionId)

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                    // Обрабатываем входящие сообщения
                    WebSocketManager.broadcast(receivedText, connectionId)
                }
            }
        } finally {
            WebSocketManager.removeConnection(connectionId)

            // Уведомляем об отключении
            val disconnectMessage = Json.encodeToString(mapOf(
                "type" to "user_disconnected",
                "userId" to userId
            ))
            WebSocketManager.broadcast(disconnectMessage)
        }
    }
}