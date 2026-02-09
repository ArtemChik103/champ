package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Запрос на добавление/обновление корзины. */
// Описывает структуру данных запроса для отправки на сервер.
data class RequestCart(
        @SerializedName("user_id") val userId: String,
        @SerializedName("product_id") val productId: String,
        @SerializedName("count") val count: Int
)
