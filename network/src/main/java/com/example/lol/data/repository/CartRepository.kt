package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.api.MatuleApi
import com.example.lol.data.network.models.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/** Реализация репозитория для работы с корзиной. */
// Инкапсулирует работу с источниками данных и обработку результатов операций.
class CartRepository(private val api: MatuleApi) : ICartRepository {

    /**
     * Добавляет сущность в целевую коллекцию или состояние.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    override suspend fun addToCart(
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseCart> {
        return try {
            val request = RequestCart(userId = userId, productId = productId, count = count)
            val response = api.createCartItem(request)

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
     * Обновляет существующую сущность и возвращает результат операции.
     *
     * @param cartItemId Идентификатор позиции корзины, которую нужно обновить.
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    override suspend fun updateCartItem(
            cartItemId: String,
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseCart> {
        return try {
            val response =
                    api.updateCartItem(
                            cartItemId = cartItemId,
                            userId = userId.toRequestBody("text/plain".toMediaTypeOrNull()),
                            productId = productId.toRequestBody("text/plain".toMediaTypeOrNull()),
                            count = count.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    )

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
