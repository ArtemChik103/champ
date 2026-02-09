package com.example.lol.data.network.models

import com.google.gson.annotations.SerializedName

/** Краткая модель продукта для списка (каталог). */
// Описывает сетевую модель данных для сериализации и десериализации.
data class ProductItem(
        @SerializedName("id") val id: String,
        @SerializedName("title") val title: String,
        @SerializedName("price") val price: Int,
        @SerializedName("typeCloses") val typeCloses: String,
        @SerializedName("type") val type: String
)
