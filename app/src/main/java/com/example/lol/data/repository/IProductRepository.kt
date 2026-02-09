package com.example.lol.data.repository

import com.example.lol.data.Product

// Определяет контракт репозитория для операций с доменными данными.
interface IProductRepository {
    // Возвращает актуальные данные из текущего источника состояния.
    fun getProducts(): List<Product>
    /**
     * Возвращает актуальные данные из текущего источника состояния.
     *
     * @param id Уникальный идентификатор сущности для целевой операции.
     */
    fun getProductById(id: Int): Product?
    /**
     * Выполняет поиск по заданному запросу и возвращает отфильтрованный результат.
     *
     * @param query Поисковый запрос для фильтрации списка.
     */
    fun searchProducts(query: String): List<Product>
}
