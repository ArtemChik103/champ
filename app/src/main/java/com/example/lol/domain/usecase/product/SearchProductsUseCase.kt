package com.example.lol.domain.usecase.product

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ProductItem
import com.example.lol.data.repository.IProductRepositoryApi

class SearchProductsUseCase(private val productRepository: IProductRepositoryApi) {
    suspend operator fun invoke(query: String): NetworkResult<List<ProductItem>> {
        return productRepository.searchProducts(query)
    }
}
