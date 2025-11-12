package ru.wtapp.models

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object Users : Table("users") {
    val id = uuid("id").uniqueIndex()
    val username = varchar("username", 50).uniqueIndex()
    val displayName = varchar("display_name", 100)
    val passwordHash = varchar("password_hash", 100)
    val avatarUrl = varchar("avatar_url", 255).nullable()
    val status = varchar("status", 20).default("offline")
    val lastSeen = datetime("last_seen").nullable()
    val createdAt = datetime("created_at").clientDefault { java.time.LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val username: String,
    val displayName: String,
    val passwordHash: String,
    val avatarUrl: String? = null,
    val status: String = "offline",
    val lastSeen: String? = null,
    val createdAt: String
)

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val status: String = "offline",
    val lastSeen: String? = null
)

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val username: String,
    val displayName: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserResponse
)

@Serializer(forClass = UUID::class)
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}