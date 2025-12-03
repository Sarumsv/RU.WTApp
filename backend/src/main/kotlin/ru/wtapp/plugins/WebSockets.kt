package ru.wtapp.plugins

import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class Connection(val session: DefaultWebSocketSession)

object WebSocketManager {
    private val connections = ConcurrentHashMap<String, Connection>()

    fun addConnection(connectionId: String, connection: Connection) {
        connections[connectionId] = connection
    }

    fun removeConnection(connectionId: String) {
        connections.remove(connectionId)
    }

    suspend fun broadcast(message: String, excludeConnectionId: String? = null) {
        connections.forEach { (id, connection) ->
            if (id != excludeConnectionId) {
                try {
                    connection.session.send(message)
                } catch (e: Exception) {
                    removeConnection(id)
                }
            }
        }
    }
}

fun Application.configureWebSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}