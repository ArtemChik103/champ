package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Ответ сервера на авторизацию.
 * @param record Данные пользователя
 * @param token JWT токен для последующих запросов
 */
data class ResponseAuth(
        @SerializedName("record") val record: User,
        @SerializedName("token") val token: String
)
