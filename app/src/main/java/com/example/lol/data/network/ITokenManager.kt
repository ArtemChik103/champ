package com.example.lol.data.network

/**
 * Интерфейс для управления токенами авторизации. Позволяет абстрагировать хранение токенов для
 * тестирования.
 */
interface ITokenManager {

    /** Сохраняет токен авторизации. */
    fun saveToken(token: String)

    /** Получает сохранённый токен. */
    fun getToken(): String?

    /** Сохраняет ID пользователя. */
    fun saveUserId(userId: String)

    /** Получает сохранённый ID пользователя. */
    fun getUserId(): String?

    /** Проверяет наличие токена. */
    fun hasToken(): Boolean

    /** Очищает все данные авторизации. */
    fun clearAuth()
}
