package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseOrder

/** Интерфейс репозитория для операций с заказами. */
// Определяет контракт репозитория для операций с доменными данными.
interface IOrderRepository {

    /**
     * Создание заказа.
     * @param userId ID пользователя
     * @param productId ID продукта
     * @param count Количество
     * @return Результат создания заказа
     */
    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    suspend fun createOrder(
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseOrder>
}
