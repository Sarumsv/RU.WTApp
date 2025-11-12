package ru.wtapp.services

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ru.wtapp.models.*
import ru.wtapp.plugins.DatabaseFactory
import java.util.*

class UserService {

    suspend fun getUserById(userId: UUID): UserResponse? {
        return DatabaseFactory.dbQuery {
            Users.select { Users.id eq userId }.singleOrNull()?.let { user ->
                UserResponse(
                    id = user[Users.id].toString(),
                    username = user[Users.username],
                    displayName = user[Users.displayName],
                    avatarUrl = user[Users.avatarUrl],
                    status = user[Users.status],
                    lastSeen = user[Users.lastSeen]?.toString()
                )
            }
        }
    }

    suspend fun searchUsers(query: String): List<UserResponse> {
        return DatabaseFactory.dbQuery {
            Users.select {
                (Users.username like "%$query%") or (Users.displayName like "%$query%")
            }.limit(20).map { user ->
                UserResponse(
                    id = user[Users.id].toString(),
                    username = user[Users.username],
                    displayName = user[Users.displayName],
                    avatarUrl = user[Users.avatarUrl],
                    status = user[Users.status]
                )
            }
        }
    }

    suspend fun updateUserStatus(userId: UUID, status: String) {
        DatabaseFactory.dbQuery {
            Users.update({ Users.id eq userId }) {
                it[Users.status] = status
                it[lastSeen] = java.time.LocalDateTime.now()
            }
        }
    }
}