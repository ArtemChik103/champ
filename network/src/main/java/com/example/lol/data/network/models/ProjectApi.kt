package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Модель проекта с сервера. */
// Описывает сетевую модель данных для сериализации и десериализации.
data class ProjectApi(
        @SerializedName("id") val id: String,
        @SerializedName("collectionId") val collectionId: String,
        @SerializedName("collectionName") val collectionName: String,
        @SerializedName("created") val created: String,
        @SerializedName("updated") val updated: String,
        @SerializedName("title") val title: String,
        @SerializedName("dateStart") val dateStart: String,
        @SerializedName("dateEnd") val dateEnd: String,
        @SerializedName("gender") val gender: String,
        @SerializedName("description_source") val descriptionSource: String,
        @SerializedName("category") val category: String,
        @SerializedName("image") val image: String,
        @SerializedName("user_id") val userId: String
)
