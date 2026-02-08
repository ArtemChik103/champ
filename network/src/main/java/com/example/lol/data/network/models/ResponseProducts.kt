package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Ответ сервера со списком продуктов (пагинация). */
data class ResponseProducts(
        @SerializedName("page") val page: Int,
        @SerializedName("perPage") val perPage: Int,
        @SerializedName("totalPages") val totalPages: Int,
        @SerializedName("totalItems") val totalItems: Int,
        @SerializedName("items") val items: List<ProductItem>
)
