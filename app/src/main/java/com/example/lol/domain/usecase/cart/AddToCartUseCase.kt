package com.example.lol.domain.usecase.cart

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseCart
import com.example.lol.data.repository.ICartRepository

class AddToCartUseCase(private val cartRepository: ICartRepository) {
    suspend operator fun invoke(
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseCart> {
        return cartRepository.addToCart(userId, productId, count)
    }
}
