package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Модель ошибки 400 от сервера. */
// Описывает сетевую модель данных для сериализации и десериализации.
data class Error400(
        @SerializedName("status") val status: Int,
        @SerializedName("message") val message: String,
        @SerializedName("data") val data: Map<String, Any>? = null
)
