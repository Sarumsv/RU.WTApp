package ru.wtapp.services

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ru.wtapp.models.*
import ru.wtapp.plugins.DatabaseFactory
import ru.wtapp.plugins.JwtConfig
import java.util.*

class AuthService {

    suspend fun register(request: RegisterRequest): AuthResponse {
        return DatabaseFactory.dbQuery {
            // Проверяем, существует ли пользователь
            val existingUser = Users.select { Users.username eq request.username }.singleOrNull()
            if (existingUser != null) {
                throw IllegalArgumentException("User with this username already exists")
            }

            // Хэшируем пароль
            val passwordHash = BCrypt.withDefaults().hashToString(12, request.password.toCharArray())

            // Создаем пользователя
            val userId = UUID.randomUUID()
            Users.insert {
                it[Users.id] = userId
                it[Users.username] = request.username
                it[Users.displayName] = request.displayName
                it[Users.passwordHash] = passwordHash
            }

            // Получаем созданного пользователя
            val user = Users.select { Users.id eq userId }.single()

            // Генерируем токен
            val token = JwtConfig.makeToken(userId.toString(), request.username)

            AuthResponse(
                token = token,
                user = UserResponse(
                    id = userId.toString(),
                    username = user[Users.username],
                    displayName = user[Users.displayName],
                    avatarUrl = user[Users.avatarUrl],
                    status = user[Users.status]
                )
            )
        }
    }

    suspend fun login(request: AuthRequest): AuthResponse {
        return DatabaseFactory.dbQuery {
            // Находим пользователя
            val user = Users.select { Users.username eq request.username }.singleOrNull()
                ?: throw IllegalArgumentException("Invalid credentials")

            // Проверяем пароль
            val result = BCrypt.verifyer().verify(request.password.toCharArray(), user[Users.passwordHash])
            if (!result.verified) {
                throw IllegalArgumentException("Invalid credentials")
            }

            // Обновляем lastSeen
            Users.update({ Users.id eq user[Users.id] }) {
                it[Users.lastSeen] = java.time.LocalDateTime.now()
                it[Users.status] = "online"
            }

            // Генерируем токен
            val token = JwtConfig.makeToken(user[Users.id].toString(), user[Users.username])

            AuthResponse(
                token = token,
                user = UserResponse(
                    id = user[Users.id].toString(),
                    username = user[Users.username],
                    displayName = user[Users.displayName],
                    avatarUrl = user[Users.avatarUrl],
                    status = "online",
                    lastSeen = user[Users.lastSeen]?.toString()
                )
            )
        }
    }
}