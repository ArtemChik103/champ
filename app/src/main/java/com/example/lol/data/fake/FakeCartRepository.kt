package com.example.lol.data.fake

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseCart
import com.example.lol.data.repository.ICartRepository

/** Fake реализация ICartRepository для unit-тестов. */
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

    fun setAddToCartSuccess(response: ResponseCart) {
        addToCartResult = NetworkResult.Success(response)
    }

    fun setAddToCartError(message: String, code: Int? = null) {
        addToCartResult = NetworkResult.Error(message, code)
    }

    fun setUpdateCartItemSuccess(response: ResponseCart) {
        updateCartItemResult = NetworkResult.Success(response)
    }

    fun setUpdateCartItemError(message: String, code: Int? = null) {
        updateCartItemResult = NetworkResult.Error(message, code)
    }

    fun reset() {
        addToCartCallCount = 0
        updateCartItemCallCount = 0
        lastAddedProductId = null
        lastAddedCount = null
        lastUpdatedCartItemId = null
        lastUpdatedCount = null
    }

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
