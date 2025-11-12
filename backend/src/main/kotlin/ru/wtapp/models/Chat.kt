package ru.wtapp.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object Chats : Table("chats") {
    val id = uuid("id").uniqueIndex()
    val type = varchar("type", 20) // "direct", "group", "channel"
    val title = varchar("title", 100).nullable()
    val avatarUrl = varchar("avatar_url", 255).nullable()
    val createdBy = uuid("created_by").references(Users.id)
    val createdAt = datetime("created_at").clientDefault { java.time.LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id)
}

object ChatMembers : Table("chat_members") {
    val id = uuid("id").uniqueIndex()
    val chatId = uuid("chat_id").references(Chats.id)
    val userId = uuid("user_id").references(Users.id)
    val role = varchar("role", 20).default("member") // "owner", "admin", "member"
    val joinedAt = datetime("joined_at").clientDefault { java.time.LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Chat(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val type: String,
    val title: String?,
    val avatarUrl: String?,
    @Serializable(with = UUIDSerializer::class)
    val createdBy: UUID,
    val createdAt: String,
    val membersCount: Int = 0,
    val lastMessage: MessagePreview? = null
)

@Serializable
data class CreateChatRequest(
    val type: String,
    val title: String? = null,
    val userIds: List<String>
)

@Serializable
data class MessagePreview(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val authorName: String,
    val text: String,
    val type: String,
    val createdAt: String
)