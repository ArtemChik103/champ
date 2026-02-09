package com.example.lol.domain.usecase.cart

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseCart
import com.example.lol.data.repository.ICartRepository

// Определяет поведение и состояние компонента в рамках текущего модуля.
class AddToCartUseCase(private val cartRepository: ICartRepository) {
    /**
     * Запускает переданный обработчик и возвращает результат его выполнения.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    suspend operator fun invoke(
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseCart> {
        return cartRepository.addToCart(userId, productId, count)
    }
}
