package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Полная модель продукта с сервера (для детальной страницы). */
data class ProductApi(
        @SerializedName("id") val id: String,
        @SerializedName("collectionId") val collectionId: String,
        @SerializedName("collectionName") val collectionName: String,
        @SerializedName("created") val created: String,
        @SerializedName("updated") val updated: String,
        @SerializedName("title") val title: String,
        @SerializedName("description") val description: String,
        @SerializedName("price") val price: Int,
        @SerializedName("typeCloses") val typeCloses: String,
        @SerializedName("type") val type: String,
        @SerializedName("approximateCost") val approximateCost: String
)
