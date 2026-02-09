package com.example.lol.domain.usecase.product

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ProductItem
import com.example.lol.data.repository.IProductRepositoryApi

// Определяет поведение и состояние компонента в рамках текущего модуля.
class SearchProductsUseCase(private val productRepository: IProductRepositoryApi) {
    /**
     * Запускает переданный обработчик и возвращает результат его выполнения.
     *
     * @param query Поисковый запрос для фильтрации списка.
     */
    suspend operator fun invoke(query: String): NetworkResult<List<ProductItem>> {
        return productRepository.searchProducts(query)
    }
}
