package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Запрос на регистрацию нового пользователя.
 * @param email Email пользователя
 * @param password Пароль пользователя
 * @param passwordConfirm Подтверждение пароля
 */
// Описывает структуру данных запроса для отправки на сервер.
data class RequestRegister(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String,
        @SerializedName("passwordConfirm") val passwordConfirm: String
)
