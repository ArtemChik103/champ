package com.example.lol.data.fake

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.News
import com.example.lol.data.network.models.ProductApi
import com.example.lol.data.network.models.ProductItem
import com.example.lol.data.repository.IProductRepositoryApi

/** Fake реализация IProductRepositoryApi для unit-тестов. */
// Инкапсулирует работу с источниками данных и обработку результатов операций.
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

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param products Список товаров для сохранения или отображения.
     */
    fun setProductsSuccess(products: List<ProductItem>) {
        productsResult = NetworkResult.Success(products)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setProductsError(message: String, code: Int? = null) {
        productsResult = NetworkResult.Error(message, code)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param product Модель товара, данные которой используются для отображения и действий.
     */
    fun setProductByIdSuccess(product: ProductApi) {
        productByIdResult = NetworkResult.Success(product)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setProductByIdError(message: String, code: Int? = null) {
        productByIdResult = NetworkResult.Error(message, code)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param products Список товаров для сохранения или отображения.
     */
    fun setSearchSuccess(products: List<ProductItem>) {
        searchResult = NetworkResult.Success(products)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setSearchError(message: String, code: Int? = null) {
        searchResult = NetworkResult.Error(message, code)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param news Список новостей, возвращаемый в тестовом сценарии.
     */
    fun setNewsSuccess(news: List<News>) {
        newsResult = NetworkResult.Success(news)
    }

    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setNewsError(message: String, code: Int? = null) {
        newsResult = NetworkResult.Error(message, code)
    }

    // Сбрасывает состояние к исходным значениям по умолчанию.
    fun reset() {
        getProductsCallCount = 0
        getProductByIdCallCount = 0
        searchProductsCallCount = 0
        getNewsCallCount = 0
        lastSearchQuery = null
        lastProductId = null
    }

    // Возвращает актуальные данные из текущего источника состояния.
    override suspend fun getProducts(): NetworkResult<List<ProductItem>> {
        getProductsCallCount++
        return productsResult
    }

    /**
     * Возвращает актуальные данные из текущего источника состояния.
     *
     * @param productId Идентификатор товара для поиска или изменения записи.
     */
    override suspend fun getProductById(productId: String): NetworkResult<ProductApi> {
        getProductByIdCallCount++
        lastProductId = productId
        return productByIdResult
    }

    /**
     * Выполняет поиск по заданному запросу и возвращает отфильтрованный результат.
     *
     * @param query Поисковый запрос для фильтрации списка.
     */
    override suspend fun searchProducts(query: String): NetworkResult<List<ProductItem>> {
        searchProductsCallCount++
        lastSearchQuery = query
        return searchResult
    }

    // Возвращает актуальные данные из текущего источника состояния.
    override suspend fun getNews(): NetworkResult<List<News>> {
        getNewsCallCount++
        return newsResult
    }
}
