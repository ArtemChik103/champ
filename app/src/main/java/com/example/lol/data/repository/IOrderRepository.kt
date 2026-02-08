package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseOrder

/** Интерфейс репозитория для операций с заказами. */
interface IOrderRepository {

    /**
     * Создание заказа.
     * @param userId ID пользователя
     * @param productId ID продукта
     * @param count Количество
     * @return Результат создания заказа
     */
    suspend fun createOrder(
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseOrder>
}
