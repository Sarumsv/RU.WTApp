package ru.wtapp.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val displayName: String,
    val passwordHash: String,
    val avatarUrl: String? = null,
    val status: String = "offline",
    val lastSeen: String? = null,
    val createdAt: String = java.time.Instant.now().toString()
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
data class AuthResponse(
    val token: String,
    val user: UserResponse
)