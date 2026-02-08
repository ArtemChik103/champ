package com.example.lol.domain.usecase.cart

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseCart
import com.example.lol.data.repository.ICartRepository

class UpdateCartItemUseCase(private val cartRepository: ICartRepository) {
    suspend operator fun invoke(
            cartItemId: String,
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseCart> {
        return cartRepository.updateCartItem(cartItemId, userId, productId, count)
    }
}
