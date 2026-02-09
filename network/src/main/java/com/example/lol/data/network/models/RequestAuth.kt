package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Запрос на авторизацию пользователя.
 * @param identity Email или идентификатор пользователя
 * @param password Пароль пользователя
 */
// Описывает структуру данных запроса для отправки на сервер.
data class RequestAuth(
        @SerializedName("identity") val identity: String,
        @SerializedName("password") val password: String
)
