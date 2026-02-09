package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseCart

/** Интерфейс репозитория для операций с корзиной. */
// Определяет контракт репозитория для операций с доменными данными.
interface ICartRepository {

        /**
         * Добавление товара в корзину.
         * @param userId ID пользователя
         * @param productId ID продукта
         * @param count Количество
         * @return Результат операции
         */
        /**
         * Добавляет сущность в целевую коллекцию или состояние.
         *
         * @param userId Идентификатор пользователя, от имени которого выполняется операция.
         * @param productId Идентификатор товара для поиска или изменения записи.
         * @param count Количество элементов для установки или изменения.
         */
        suspend fun addToCart(
                userId: String,
                productId: String,
                count: Int
        ): NetworkResult<ResponseCart>

        /**
         * Обновление количества товара в корзине.
         * @param cartItemId ID элемента корзины
         * @param userId ID пользователя
         * @param productId ID продукта
         * @param count Новое количество
         * @return Результат операции
         */
        /**
         * Обновляет существующую сущность и возвращает результат операции.
         *
         * @param cartItemId Идентификатор позиции корзины, которую нужно обновить.
         * @param userId Идентификатор пользователя, от имени которого выполняется операция.
         * @param productId Идентификатор товара для поиска или изменения записи.
         * @param count Количество элементов для установки или изменения.
         */
        suspend fun updateCartItem(
                cartItemId: String,
                userId: String,
                productId: String,
                count: Int
        ): NetworkResult<ResponseCart>
}
