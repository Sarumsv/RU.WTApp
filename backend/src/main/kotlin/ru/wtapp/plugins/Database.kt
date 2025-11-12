package ru.wtapp.plugins

// Временная заглушка - база данных в памяти
object InMemoryDatabase {
    private val users = mutableListOf<User>()

    fun addUser(user: User) {
        users.add(user)
    }

    fun findUserByUsername(username: String): User? {
        return users.find { it.username == username }
    }

    fun getAllUsers(): List<User> {
        return users.toList()
    }
}

// Временный класс User для in-memory базы
data class User(
    val id: String,
    val username: String,
    val displayName: String,
    val passwordHash: String,
    val avatarUrl: String? = null
)