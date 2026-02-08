package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseCart

/** Интерфейс репозитория для операций с корзиной. */
interface ICartRepository {

        /**
         * Добавление товара в корзину.
         * @param userId ID пользователя
         * @param productId ID продукта
         * @param count Количество
         * @return Результат операции
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
        suspend fun updateCartItem(
                cartItemId: String,
                userId: String,
                productId: String,
                count: Int
        ): NetworkResult<ResponseCart>
}
