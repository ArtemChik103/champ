package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Ответ сервера на регистрацию пользователя. */
// Описывает структуру ответа, получаемого от API.
data class ResponseRegister(
        @SerializedName("id") val id: String,
        @SerializedName("collectionId") val collectionId: String,
        @SerializedName("collectionName") val collectionName: String,
        @SerializedName("created") val created: String,
        @SerializedName("updated") val updated: String,
        @SerializedName("emailVisibility") val emailVisibility: Boolean,
        @SerializedName("firstname") val firstname: String,
        @SerializedName("lastname") val lastname: String,
        @SerializedName("secondname") val secondname: String,
        @SerializedName("verified") val verified: Boolean,
        @SerializedName("datebirthday") val datebirthday: String,
        @SerializedName("gender") val gender: String
)
