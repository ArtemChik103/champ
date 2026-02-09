package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.api.MatuleApi
import com.example.lol.data.network.models.*

/** Реализация репозитория для работы с продуктами через API. */
// Определяет поведение и состояние компонента в рамках текущего модуля.
class ProductRepositoryApi(private val api: MatuleApi) : IProductRepositoryApi {

    // Возвращает актуальные данные из текущего источника состояния.
    override suspend fun getProducts(): NetworkResult<List<ProductItem>> {
        return try {
            val response = api.getProducts()

            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.items)
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                NetworkResult.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Ошибка сети")
        }
    }

    /**
     * Возвращает актуальные данные из текущего источника состояния.
     *
     * @param productId Идентификатор товара для поиска или изменения записи.
     */
    override suspend fun getProductById(productId: String): NetworkResult<ProductApi> {
        return try {
            val response = api.getProduct(productId)

            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                NetworkResult.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Ошибка сети")
        }
    }

    /**
     * Выполняет поиск по заданному запросу и возвращает отфильтрованный результат.
     *
     * @param query Поисковый запрос для фильтрации списка.
     */
    override suspend fun searchProducts(query: String): NetworkResult<List<ProductItem>> {
        return try {
            // Формируем фильтр согласно API: "(title ?~ 'query')"
            // Формируем фильтр согласно API: "(title ?~ 'query')".
            val filter = "(title ?~ '$query')"
            val response = api.getProducts(filter)

            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.items)
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                NetworkResult.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Ошибка сети")
        }
    }

    // Возвращает актуальные данные из текущего источника состояния.
    override suspend fun getNews(): NetworkResult<List<News>> {
        return try {
            val response = api.getNews()

            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!.items)
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                NetworkResult.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Ошибка сети")
        }
    }

    /**
     * Разбирает входные данные и формирует нормализованный результат.
     *
     * @param errorBody Сырые данные ошибки от сервера для извлечения сообщения.
     */
    private fun parseErrorMessage(errorBody: String?): String {
        if (errorBody == null) return "Неизвестная ошибка"

        return try {
            val gson = com.google.gson.Gson()
            val error = gson.fromJson(errorBody, Error400::class.java)
            error.message
        } catch (e: Exception) {
            errorBody.take(200)
        }
    }
}
