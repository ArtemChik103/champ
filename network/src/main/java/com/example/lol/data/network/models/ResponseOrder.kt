package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Ответ сервера на создание заказа. */
// Описывает структуру ответа, получаемого от API.
data class ResponseOrder(
        @SerializedName("id") val id: String,
        @SerializedName("collectionId") val collectionId: String,
        @SerializedName("collectionName") val collectionName: String,
        @SerializedName("created") val created: String,
        @SerializedName("updated") val updated: String,
        @SerializedName("user_id") val userId: String,
        @SerializedName("product_id") val productId: String,
        @SerializedName("count") val count: Int
)
