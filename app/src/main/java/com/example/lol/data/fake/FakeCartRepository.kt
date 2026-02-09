package com.example.lol.data.fake

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseCart
import com.example.lol.data.repository.ICartRepository

/** Fake реализация ICartRepository для unit-тестов. */
// Инкапсулирует работу с источниками данных и обработку результатов операций.
class FakeCartRepository : ICartRepository {

    private var addToCartResult: NetworkResult<ResponseCart> = NetworkResult.Error("Not configured")
    private var updateCartItemResult: NetworkResult<ResponseCart> =
            NetworkResult.Error("Not configured")

    var addToCartCallCount = 0
        private set
    var updateCartItemCallCount = 0
        private set

    var lastAddedProductId: String? = null
        private set
    var lastAddedCount: Int? = null
        private set
    var lastUpdatedCartItemId: String? = null
        private set
    var lastUpdatedCount: Int? = null
        private set

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param response Ответ сервера, который нужно преобразовать в результат операции.
     */
    fun setAddToCartSuccess(response: ResponseCart) {
        addToCartResult = NetworkResult.Success(response)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setAddToCartError(message: String, code: Int? = null) {
        addToCartResult = NetworkResult.Error(message, code)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param response Ответ сервера, который нужно преобразовать в результат операции.
     */
    fun setUpdateCartItemSuccess(response: ResponseCart) {
        updateCartItemResult = NetworkResult.Success(response)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setUpdateCartItemError(message: String, code: Int? = null) {
        updateCartItemResult = NetworkResult.Error(message, code)
    }

    // Сбрасывает состояние к исходным значениям по умолчанию.
    fun reset() {
        addToCartCallCount = 0
        updateCartItemCallCount = 0
        lastAddedProductId = null
        lastAddedCount = null
        lastUpdatedCartItemId = null
        lastUpdatedCount = null
    }

    /**
     * Добавляет сущность в целевую коллекцию или состояние.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    override suspend fun addToCart(
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseCart> {
        addToCartCallCount++
        lastAddedProductId = productId
        lastAddedCount = count
        return addToCartResult
    }

    /**
     * Обновляет существующую сущность и возвращает результат операции.
     *
     * @param cartItemId Идентификатор позиции корзины, которую нужно обновить.
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    override suspend fun updateCartItem(
            cartItemId: String,
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseCart> {
        updateCartItemCallCount++
        lastUpdatedCartItemId = cartItemId
        lastUpdatedCount = count
        return updateCartItemResult
    }
}
