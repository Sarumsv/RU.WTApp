// backend/src/main/kotlin/ru/wtapp/models/dto/AuthDto.kt
package ru.wtapp.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
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
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val status: String = "offline"
)