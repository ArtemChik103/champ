package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.News
import com.example.lol.data.network.models.ProductApi
import com.example.lol.data.network.models.ProductItem

/**
 * Интерфейс репозитория для работы с продуктами и новостями. Расширяет функциональность для работы
 * с API.
 */
interface IProductRepositoryApi {

    /**
     * Получение списка продуктов.
     * @return Список продуктов
     */
    suspend fun getProducts(): NetworkResult<List<ProductItem>>

    /**
     * Получение детальной информации о продукте.
     * @param productId ID продукта
     * @return Детальная информация о продукте
     */
    suspend fun getProductById(productId: String): NetworkResult<ProductApi>

    /**
     * Поиск продуктов по запросу.
     * @param query Поисковый запрос
     * @return Отфильтрованный список продуктов
     */
    suspend fun searchProducts(query: String): NetworkResult<List<ProductItem>>

    /**
     * Получение списка новостей/акций.
     * @return Список новостей
     */
    suspend fun getNews(): NetworkResult<List<News>>
}
