package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Запрос на создание проекта. */
data class RequestProject(
        @SerializedName("title") val title: String,
        @SerializedName("typeProject") val typeProject: String,
        @SerializedName("user_id") val userId: String,
        @SerializedName("dateStart") val dateStart: String,
        @SerializedName("dateEnd") val dateEnd: String,
        @SerializedName("gender") val gender: String,
        @SerializedName("description_source") val descriptionSource: String,
        @SerializedName("category") val category: String

// Примечание: image отправляется отдельно как multipart
)
