package com.example.lol.data.repository

import com.example.lol.data.network.ITokenManager
import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.api.MatuleApi
import com.example.lol.data.network.models.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/** Реализация репозитория авторизации. Выполняет сетевые запросы через MatuleApi. */
class AuthRepository(private val api: MatuleApi, private val tokenManager: ITokenManager) :
        IAuthRepository {

    override suspend fun register(
            email: String,
            password: String
    ): NetworkResult<ResponseRegister> {
        return try {
            val request =
                    RequestRegister(email = email, password = password, passwordConfirm = password)
            val response = api.register(request)

            if (response.isSuccessful && response.body() != null) {
                val responseString = response.body()!!.string().trim()
                val gson = com.google.gson.Gson()

                val registerResponse =
                        try {
                            gson.fromJson(responseString, ResponseRegister::class.java)
                        } catch (e: Exception) {
                            return NetworkResult.Error("Ошибка регистрации")
                        }

                if (registerResponse == null || registerResponse.id.isBlank()) {
                    return NetworkResult.Error("Ошибка регистрации")
                }

                NetworkResult.Success(registerResponse)
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                NetworkResult.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Ошибка сети")
        }
    }

    override suspend fun login(email: String, password: String): NetworkResult<ResponseAuth> {
        return try {
            val request = RequestAuth(identity = email, password = password)
            val response = api.auth(request)

            if (response.isSuccessful && response.body() != null) {
                val responseString = response.body()!!.string().trim()
                val gson = com.google.gson.Gson()

                val authResponse =
                        try {
                            gson.fromJson(responseString, ResponseAuth::class.java)
                        } catch (e: Exception) {
                            return NetworkResult.Error("Неверный email или пароль")
                        }

                // Проверяем что ответ содержит необходимые данные
                if (authResponse == null ||
                                authResponse.token.isBlank() ||
                                authResponse.record.id.isBlank()
                ) {
                    return NetworkResult.Error("Неверный email или пароль")
                }

                // Сохраняем токен и ID пользователя, предварительно очистив его от недопустимых
                // символов
                val cleanToken = authResponse.token.replace("\n", "").replace("\r", "").trim()
                tokenManager.saveToken(cleanToken)
                tokenManager.saveUserId(authResponse.record.id)

                NetworkResult.Success(authResponse)
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                NetworkResult.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Ошибка сети")
        }
    }

    override suspend fun getUser(userId: String): NetworkResult<User> {
        return try {
            val response = api.getUser(userId)

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

    override suspend fun updateUser(
            userId: String,
            firstname: String?,
            lastname: String?,
            secondname: String?,
            datebirthday: String?,
            gender: String?
    ): NetworkResult<User> {
        return try {
            val response =
                    api.updateUser(
                            userId = userId,
                            firstname = firstname?.toRequestBody("text/plain".toMediaTypeOrNull()),
                            lastname = lastname?.toRequestBody("text/plain".toMediaTypeOrNull()),
                            secondname =
                                    secondname?.toRequestBody("text/plain".toMediaTypeOrNull()),
                            datebirthday =
                                    datebirthday?.toRequestBody("text/plain".toMediaTypeOrNull()),
                            gender = gender?.toRequestBody("text/plain".toMediaTypeOrNull())
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

    override suspend fun logout(): NetworkResult<Unit> {
        return try {
            // Получаем список токенов авторизации
            val authResponse = api.getUsersAuth()

            if (authResponse.isSuccessful && authResponse.body() != null) {
                val usersAuth = authResponse.body()!!
                val currentUserId = tokenManager.getUserId()

                // Находим токен текущего пользователя
                val tokenToDelete = usersAuth.items.find { it.recordRef == currentUserId }

                if (tokenToDelete != null) {
                    val deleteResponse = api.logout(tokenToDelete.id)
                    if (deleteResponse.isSuccessful) {
                        tokenManager.clearAuth()
                        NetworkResult.Success(Unit)
                    } else {
                        NetworkResult.Error("Не удалось выйти из системы", deleteResponse.code())
                    }
                } else {
                    // Токен не найден, всё равно очищаем локальные данные
                    tokenManager.clearAuth()
                    NetworkResult.Success(Unit)
                }
            } else {
                // Даже если ошибка, очищаем локальные данные
                tokenManager.clearAuth()
                NetworkResult.Success(Unit)
            }
        } catch (e: Exception) {
            // При ошибке сети всё равно очищаем локальные данные
            tokenManager.clearAuth()
            NetworkResult.Error(e.message ?: "Ошибка сети")
        }
    }

    /** Парсит сообщение об ошибке из JSON ответа. */
    private fun parseErrorMessage(errorBody: String?): String {
        if (errorBody == null) return "Неизвестная ошибка"

        return try {
            // Пытаемся распарсить как Error400
            val gson = com.google.gson.Gson()
            val error = gson.fromJson(errorBody, Error400::class.java)
            error.message
        } catch (e: Exception) {
            errorBody.take(200)
        }
    }
}
