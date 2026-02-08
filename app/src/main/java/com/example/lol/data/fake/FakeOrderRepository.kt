package com.example.lol.data.fake

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseOrder
import com.example.lol.data.repository.IOrderRepository

/** Fake реализация IOrderRepository для unit-тестов. */
class FakeOrderRepository : IOrderRepository {

    private var createOrderResult: NetworkResult<ResponseOrder> =
            NetworkResult.Error("Not configured")

    var createOrderCallCount = 0
        private set

    var lastOrderProductId: String? = null
        private set
    var lastOrderCount: Int? = null
        private set

    fun setCreateOrderSuccess(response: ResponseOrder) {
        createOrderResult = NetworkResult.Success(response)
    }

    fun setCreateOrderError(message: String, code: Int? = null) {
        createOrderResult = NetworkResult.Error(message, code)
    }

    fun reset() {
        createOrderCallCount = 0
        lastOrderProductId = null
        lastOrderCount = null
    }

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
