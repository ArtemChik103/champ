package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Запрос на обновление профиля пользователя. */
data class RequestUser(
        @SerializedName("email") val email: String? = null,
        @SerializedName("emailVisibility") val emailVisibility: Boolean? = null,
        @SerializedName("firstname") val firstname: String? = null,
        @SerializedName("lastname") val lastname: String? = null,
        @SerializedName("secondname") val secondname: String? = null,
        @SerializedName("datebirthday") val datebirthday: String? = null,
        @SerializedName("gender") val gender: String? = null
)
