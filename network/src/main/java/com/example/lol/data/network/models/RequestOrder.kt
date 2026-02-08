package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Запрос на создание заказа. */
data class RequestOrder(
        @SerializedName("user_id") val userId: String,
        @SerializedName("product_id") val productId: String,
        @SerializedName("count") val count: Int
)
