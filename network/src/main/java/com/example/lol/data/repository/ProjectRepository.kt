package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.api.MatuleApi
import com.example.lol.data.network.models.*
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/** Реализация репозитория для работы с проектами. */
// Инкапсулирует работу с источниками данных и обработку результатов операций.
class ProjectRepository(private val api: MatuleApi) : IProjectRepository {

    // Возвращает актуальные данные из текущего источника состояния.
    override suspend fun getProjects(): NetworkResult<List<ProjectApi>> {
        return try {
            val response = api.getProjects()

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
     * Создает новую сущность на основе переданных данных.
     *
     * @param title Заголовок, который отображается в интерфейсе.
     * @param typeProject Тип проекта, отправляемый при создании записи.
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param dateStart Значение времени `dateStart` для вычислений или форматирования.
     * @param dateEnd Значение времени `dateEnd` для вычислений или форматирования.
     * @param gender Выбранный пол пользователя или проекта.
     * @param descriptionSource Описание проекта, которое передается в API.
     * @param category Категория, по которой выполняется фильтрация или сохранение.
     * @param image Файл изображения, прикрепляемый к сетевому запросу.
     */
    override suspend fun createProject(
            title: String,
            typeProject: String,
            userId: String,
            dateStart: String,
            dateEnd: String,
            gender: String,
            descriptionSource: String,
            category: String,
            image: File?
    ): NetworkResult<ProjectApi> {
        return try {
            val imagePart =
                    image?.let {
                        val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("image", it.name, requestFile)
                    }

            val response =
                    api.createProject(
                            title = title.toRequestBody("text/plain".toMediaTypeOrNull()),
                            typeProject =
                                    typeProject.toRequestBody("text/plain".toMediaTypeOrNull()),
                            userId = userId.toRequestBody("text/plain".toMediaTypeOrNull()),
                            dateStart = dateStart.toRequestBody("text/plain".toMediaTypeOrNull()),
                            dateEnd = dateEnd.toRequestBody("text/plain".toMediaTypeOrNull()),
                            gender = gender.toRequestBody("text/plain".toMediaTypeOrNull()),
                            descriptionSource =
                                    descriptionSource.toRequestBody(
                                            "text/plain".toMediaTypeOrNull()
                                    ),
                            category = category.toRequestBody("text/plain".toMediaTypeOrNull()),
                            image = imagePart
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
