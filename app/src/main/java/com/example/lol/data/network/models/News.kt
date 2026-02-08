package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Модель новости/акции. */
data class News(
        @SerializedName("id") val id: String,
        @SerializedName("collectionId") val collectionId: String,
        @SerializedName("collectionName") val collectionName: String,
        @SerializedName("newsImage") val newsImage: String,
        @SerializedName("created") val created: String,
        @SerializedName("updated") val updated: String
)
