package com.example.lol.data.fake

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseOrder
import com.example.lol.data.repository.IOrderRepository

/** Fake реализация IOrderRepository для unit-тестов. */
// Инкапсулирует работу с источниками данных и обработку результатов операций.
class FakeOrderRepository : IOrderRepository {

    private var createOrderResult: NetworkResult<ResponseOrder> =
            NetworkResult.Error("Not configured")

    var createOrderCallCount = 0
        private set

    var lastOrderProductId: String? = null
        private set
    var lastOrderCount: Int? = null
        private set

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param response Ответ сервера, который нужно преобразовать в результат операции.
     */
    fun setCreateOrderSuccess(response: ResponseOrder) {
        createOrderResult = NetworkResult.Success(response)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setCreateOrderError(message: String, code: Int? = null) {
        createOrderResult = NetworkResult.Error(message, code)
    }

    // Сбрасывает состояние к исходным значениям по умолчанию.
    fun reset() {
        createOrderCallCount = 0
        lastOrderProductId = null
        lastOrderCount = null
    }

    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    override suspend fun createOrder(
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseOrder> {
        createOrderCallCount++
        lastOrderProductId = productId
        lastOrderCount = count
        return createOrderResult
    }
}
