package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Элемент авторизации пользователя для получения токена. */
data class UserAuth(
        @SerializedName("id") val id: String,
        @SerializedName("collectionId") val collectionId: String,
        @SerializedName("collectionName") val collectionName: String,
        @SerializedName("created") val created: String,
        @SerializedName("updated") val updated: String,
        @SerializedName("collectionRef") val collectionRef: String,
        @SerializedName("fingerprint") val fingerprint: String,
        @SerializedName("recordRef") val recordRef: String
)

/** Ответ сервера со списком токенов авторизации. */
data class UsersAuth(
        @SerializedName("page") val page: Int,
        @SerializedName("perPage") val perPage: Int,
        @SerializedName("totalPages") val totalPages: Int,
        @SerializedName("totalItems") val totalItems: Int,
        @SerializedName("items") val items: List<UserAuth>
)
