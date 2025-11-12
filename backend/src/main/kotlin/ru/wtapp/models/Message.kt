package ru.wtapp.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object Messages : Table("messages") {
    val id = uuid("id").uniqueIndex()
    val chatId = uuid("chat_id").references(Chats.id)
    val authorId = uuid("author_id").references(Users.id)
    val type = varchar("type", 20) // "text", "image", "voice"
    val content = text("content")
    val attachmentUrl = varchar("attachment_url", 255).nullable()
    val duration = integer("duration").nullable() // для голосовых сообщений
    val createdAt = datetime("created_at").clientDefault { java.time.LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Message(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val chatId: UUID,
    val author: UserResponse,
    val type: String,
    val content: String,
    val attachmentUrl: String? = null,
    val duration: Int? = null,
    val reactions: Map<String, Int> = emptyMap(),
    val createdAt: String
)

@Serializable
data class SendMessageRequest(
    val type: String,
    val content: String,
    val attachmentUrl: String? = null,
    val duration: Int? = null
)