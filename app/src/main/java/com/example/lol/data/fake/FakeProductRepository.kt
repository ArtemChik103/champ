package com.example.lol.data.fake

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.News
import com.example.lol.data.network.models.ProductApi
import com.example.lol.data.network.models.ProductItem
import com.example.lol.data.repository.IProductRepositoryApi

/** Fake реализация IProductRepositoryApi для unit-тестов. */
class FakeProductRepository : IProductRepositoryApi {

    private var productsResult: NetworkResult<List<ProductItem>> =
            NetworkResult.Success(emptyList())
    private var productByIdResult: NetworkResult<ProductApi> = NetworkResult.Error("Not configured")
    private var searchResult: NetworkResult<List<ProductItem>> = NetworkResult.Success(emptyList())
    private var newsResult: NetworkResult<List<News>> = NetworkResult.Success(emptyList())

    var getProductsCallCount = 0
        private set
    var getProductByIdCallCount = 0
        private set
    var searchProductsCallCount = 0
        private set
    var getNewsCallCount = 0
        private set

    var lastSearchQuery: String? = null
        private set
    var lastProductId: String? = null
        private set

    fun setProductsSuccess(products: List<ProductItem>) {
        productsResult = NetworkResult.Success(products)
    }

    fun setProductsError(message: String, code: Int? = null) {
        productsResult = NetworkResult.Error(message, code)
    }

    fun setProductByIdSuccess(product: ProductApi) {
        productByIdResult = NetworkResult.Success(product)
    }

    fun setProductByIdError(message: String, code: Int? = null) {
        productByIdResult = NetworkResult.Error(message, code)
    }

    fun setSearchSuccess(products: List<ProductItem>) {
        searchResult = NetworkResult.Success(products)
    }

    fun setSearchError(message: String, code: Int? = null) {
        searchResult = NetworkResult.Error(message, code)
    }

    fun setNewsSuccess(news: List<News>) {
        newsResult = NetworkResult.Success(news)
    }

    fun setNewsError(message: String, code: Int? = null) {
        newsResult = NetworkResult.Error(message, code)
    }

    fun reset() {
        getProductsCallCount = 0
        getProductByIdCallCount = 0
        searchProductsCallCount = 0
        getNewsCallCount = 0
        lastSearchQuery = null
        lastProductId = null
    }

    override suspend fun getProducts(): NetworkResult<List<ProductItem>> {
        getProductsCallCount++
        return productsResult
    }

    override suspend fun getProductById(productId: String): NetworkResult<ProductApi> {
        getProductByIdCallCount++
        lastProductId = productId
        return productByIdResult
    }

    override suspend fun searchProducts(query: String): NetworkResult<List<ProductItem>> {
        searchProductsCallCount++
        lastSearchQuery = query
        return searchResult
    }

    override suspend fun getNews(): NetworkResult<List<News>> {
        getNewsCallCount++
        return newsResult
    }
}
