package com.example.lol.data.network

import android.content.Context
import android.content.SharedPreferences

/**
 * Менеджер для хранения и управления Bearer токеном авторизации. Использует SharedPreferences для
 * сохранения токена между сессиями.
 */
// Управляет локальными данными и настройками, связанными с пользовательской сессией.
class TokenManager(context: Context) : ITokenManager {

    private val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    init {
        // Автоочистка поврежденных данных при инициализации
        try {
            val token = prefs.getString(KEY_TOKEN, null)
            // Если токен содержит символы вне ASCII (например кириллицу из прошлой ошибки), очищаем
            // всё
            // Если токен содержит символы вне ASCII (например кириллицу из прошлой ошибки), очищаем его.
            // всё
            if (token != null && !token.all { it.code in 0x20..0x7E }) {
                prefs.edit().clear().apply()
            }
        } catch (e: Exception) {
            // Если файл SharedPreferences поврежден и выбрасывает исключение при чтении
            // (XmlPullParserException)
            // Если файл SharedPreferences повреждён и выбрасывает исключение при чтении,
            // Исключение XmlPullParserException.
            prefs.edit().clear().apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "matule_auth_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
    }

    /**
     * Сохраняет токен авторизации.
     * @param token JWT токен от сервера
     */
    /**
     * Сохраняет переданные данные в целевое хранилище.
     *
     * @param token Токен авторизации для выполнения защищенных запросов.
     */
    override fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    /**
     * Получает сохранённый токен.
     * @return токен или null если не сохранён
     */
    // Возвращает актуальные данные из текущего источника состояния.
    override fun getToken(): String? {
        return try {
            prefs.getString(KEY_TOKEN, null)?.replace("\n", "")?.replace("\r", "")?.trim()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Сохраняет ID пользователя.
     * @param userId ID пользователя от сервера
     */
    /**
     * Сохраняет переданные данные в целевое хранилище.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     */
    override fun saveUserId(userId: String) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    /**
     * Получает сохранённый ID пользователя.
     * @return ID пользователя или null если не сохранён
     */
    // Возвращает актуальные данные из текущего источника состояния.
    override fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    /**
     * Проверяет наличие токена.
     * @return true если токен сохранён
     */
    // Проверяет наличие данных или состояния и возвращает булев результат.
    override fun hasToken(): Boolean {
        return !getToken().isNullOrBlank()
    }

    /** Очищает все данные авторизации. */
    // Очищает связанные данные и приводит состояние к пустому виду.
    override fun clearAuth() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_USER_ID).apply()
    }
}
