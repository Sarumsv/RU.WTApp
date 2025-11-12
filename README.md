```markdown
# 💬 WTApp - Мультиплатформенный мессенджер

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.10-blue.svg)](https://kotlinlang.org)
[![Ktor](https://img.shields.io/badge/Ktor-2.3.5-orange.svg)](https://ktor.io)
[![KMM](https://img.shields.io/badge/Kotlin-Multiplatform-purple.svg)](https://kotlinlang.org/lp/multiplatform/)

Современный мессенджер с поддержкой текстовых, голосовых сообщений и изображений для всех платформ.

## 📋 Функциональность

### ✅ Реализовано (Backend)
- **JWT аутентификация** (регистрация, логин)
- **REST API** для управления пользователями и чатами
- **WebSocket** для реального времени
- **PostgreSQL** база данных
- **CORS** поддержка для фронтенда

### 🚧 В разработке
- Мобильные клиенты (Android/iOS через KMM)
- Голосовые сообщения
- Групповые чаты и каналы
- Медиа-вложения

## 🏗️ Архитектура

```
RU.WTApp/
├── backend/                 # Ktor сервер
│   ├── src/main/kotlin/ru/wtapp/
│   │   ├── plugins/        # Конфигурация (Auth, DB, WebSockets)
│   │   ├── routes/         # REST API маршруты
│   │   ├── services/       # Бизнес-логика
│   │   ├── models/         # Data-модели и Exposed таблицы
│   │   └── Application.kt  # Точка входа
│   ├── docker-compose.yml  # PostgreSQL конфигурация
│   └── build.gradle.kts
├── shared/                 # KMM общий модуль (в разработке)
├── android/               # Android клиент (в разработке)
└── ios/                   # iOS клиент (в разработке)
```

## 🚀 Быстрый старт

### Предварительные требования
- Java 17+
- Docker & Docker Compose
- PostgreSQL 15

### 1. Запуск базы данных
```bash
cd backend
docker-compose up -d
```

### 2. Сборка и запуск сервера
```bash
cd backend
./gradlew build
./gradlew run
```

Сервер будет доступен по адресу: `http://localhost:8080`

## 📡 API Документация

### Базовые эндпоинты
- `GET /` - Статус сервера
- `GET /health` - Проверка здоровья

### Аутентификация
```http
POST /auth/register
Content-Type: application/json

{
  "username": "user1",
  "displayName": "Иван Иванов",
  "password": "pass123"
}
```

```http
POST /auth/login
Content-Type: application/json

{
  "username": "user1",
  "password": "pass123"
}
```

### Пользователи (требует JWT)
```http
GET /users/me
Authorization: Bearer <jwt-token>

GET /users/search?query=user
Authorization: Bearer <jwt-token>
```

### WebSocket
```http
WS /ws?token=<jwt-token>
```

## 🛠️ Технологический стек

### Backend
- **Ktor 2.3.5** - Асинхронный фреймворк
- **PostgreSQL** - Реляционная база данных
- **Exposed** - ORM для Kotlin
- **JWT** - Аутентификация
- **HikariCP** - Пул соединений БД
- **BCrypt** - Хэширование паролей

### Мобильная разработка (планируется)
- **KMM (Kotlin Multiplatform Mobile)** - Общая бизнес-логика
- **Jetpack Compose** - Android UI
- **SwiftUI** - iOS UI

## 🔧 Разработка

### Структура проекта Backend
```
backend/src/main/kotlin/ru/wtapp/
├── Application.kt          # Конфигурация сервера
├── plugins/
│   ├── Authentication.kt   # JWT аутентификация
│   ├── Database.kt         # PostgreSQL + Exposed
│   └── WebSockets.kt       # WebSocket управление
├── routes/
│   ├── AuthRoutes.kt       # /auth эндпоинты
│   ├── UserRoutes.kt       # /users эндпоинты
│   └── WebSocketRoutes.kt  # WebSocket обработка
├── services/
│   ├── AuthService.kt      # Логика аутентификации
│   └── UserService.kt      # Логика пользователей
└── models/
    ├── User.kt             # Модели пользователей
    ├── Chat.kt             # Модели чатов
    └── Message.kt          # Модели сообщений
```

### Модели данных
```kotlin
// Пользователь
data class User(
    val id: UUID,
    val username: String,
    val displayName: String,
    val status: String = "offline"
)

// Чат
data class Chat(
    val id: UUID,
    val type: String, // "direct", "group", "channel"
    val title: String?,
    val membersCount: Int
)

// Сообщение
data class Message(
    val id: UUID,
    val chatId: UUID,
    val author: UserResponse,
    val type: String, // "text", "image", "voice"
    val content: String
)
```

## 🧪 Тестирование API

### PowerShell (Windows)
```powershell
# Регистрация
$body = @{
    username = "user1"
    displayName = "Test User"
    password = "pass123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/auth/register" `
    -Method POST `
    -Headers @{"Content-Type" = "application/json"} `
    -Body $body
```

### Bash (Linux/Mac)
```bash
# Регистрация
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","displayName":"Test User","password":"pass123"}'

# Логин
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"pass123"}'
```

## 📈 Планы развития

### Ближайшие задачи
- [ ] Реализация KMM shared модуля
- [ ] Android клиент с Jetpack Compose
- [ ] iOS клиент с SwiftUI
- [ ] Голосовые сообщения
- [ ] Групповые чаты
- [ ] Отправка изображений

### Будущие возможности
- [ ] Каналы (Telegram-style)
- [ ] Push-уведомления
- [ ] Шифрование сообщений
- [ ] Видео сообщения
- [ ] Реакции на сообщения

## 🤝 Участие в разработке

1. Форкните репозиторий
2. Создайте feature ветку (`git checkout -b feature/amazing-feature`)
3. Закоммитьте изменения (`git commit -m 'Add amazing feature'`)
4. Запушьте ветку (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

## 📄 Лицензия

Этот проект лицензирован под MIT License - смотрите файл [LICENSE](LICENSE) для деталей.

## 👥 Авторы

- **Sarumsv** - Initial work - [Sarumsv](https://github.com/Sarumsv)

---

**WTApp** - Общайтесь с удовольствием! 💬✨
```
