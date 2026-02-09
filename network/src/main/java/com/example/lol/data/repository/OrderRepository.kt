package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.api.MatuleApi
import com.example.lol.data.network.models.*

/** Реализация репозитория для работы с заказами. */
// Инкапсулирует работу с источниками данных и обработку результатов операций.
class OrderRepository(private val api: MatuleApi) : IOrderRepository {

    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param productId Идентификатор товара для поиска или изменения записи.
     * @param count Количество элементов для установки или изменения.
     */
    override suspend fun createOrder(
            userId: String,
            productId: String,
            count: Int
    ): NetworkResult<ResponseOrder> {
        return try {
            val request = RequestOrder(userId = userId, productId = productId, count = count)
            val response = api.createOrder(request)

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
