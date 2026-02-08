package com.example.lol.data.fake

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseAuth
import com.example.lol.data.network.models.ResponseRegister
import com.example.lol.data.network.models.User
import com.example.lol.data.repository.IAuthRepository

/**
 * Fake реализация IAuthRepository для unit-тестов. Позволяет настраивать ответы без реальных
 * сетевых запросов.
 */
class FakeAuthRepository : IAuthRepository {

    private var registerResult: NetworkResult<ResponseRegister> =
            NetworkResult.Error("Not configured")
    private var loginResult: NetworkResult<ResponseAuth> = NetworkResult.Error("Not configured")
    private var getUserResult: NetworkResult<User> = NetworkResult.Error("Not configured")
    private var updateUserResult: NetworkResult<User> = NetworkResult.Error("Not configured")
    private var logoutResult: NetworkResult<Unit> = NetworkResult.Success(Unit)

    // Счётчики вызовов для верификации
    var registerCallCount = 0
        private set
    var loginCallCount = 0
        private set
    var getUserCallCount = 0
        private set
    var updateUserCallCount = 0
        private set
    var logoutCallCount = 0
        private set

    // Последние переданные параметры
    var lastRegisterEmail: String? = null
        private set
    var lastLoginEmail: String? = null
        private set
    var lastLoginPassword: String? = null
        private set

    /** Настраивает успешный ответ на регистрацию. */
    fun setRegisterSuccess(response: ResponseRegister) {
        registerResult = NetworkResult.Success(response)
    }

    /** Настраивает ошибку регистрации. */
    fun setRegisterError(message: String, code: Int? = null) {
        registerResult = NetworkResult.Error(message, code)
    }

    /** Настраивает успешный ответ на авторизацию. */
    fun setLoginSuccess(response: ResponseAuth) {
        loginResult = NetworkResult.Success(response)
    }

    /** Настраивает ошибку авторизации. */
    fun setLoginError(message: String, code: Int? = null) {
        loginResult = NetworkResult.Error(message, code)
    }

    /** Настраивает успешный ответ получения пользователя. */
    fun setGetUserSuccess(user: User) {
        getUserResult = NetworkResult.Success(user)
    }

    /** Настраивает ошибку получения пользователя. */
    fun setGetUserError(message: String, code: Int? = null) {
        getUserResult = NetworkResult.Error(message, code)
    }

    /** Настраивает успешный ответ обновления пользователя. */
    fun setUpdateUserSuccess(user: User) {
        updateUserResult = NetworkResult.Success(user)
    }

    /** Настраивает ошибку обновления пользователя. */
    fun setUpdateUserError(message: String, code: Int? = null) {
        updateUserResult = NetworkResult.Error(message, code)
    }

    /** Настраивает ошибку logout. */
    fun setLogoutError(message: String) {
        logoutResult = NetworkResult.Error(message)
    }

    /** Сброс всех счётчиков и настроек. */
    fun reset() {
        registerCallCount = 0
        loginCallCount = 0
        getUserCallCount = 0
        updateUserCallCount = 0
        logoutCallCount = 0
        lastRegisterEmail = null
        lastLoginEmail = null
        lastLoginPassword = null
    }

    override suspend fun register(
            email: String,
            password: String
    ): NetworkResult<ResponseRegister> {
        registerCallCount++
        lastRegisterEmail = email
        return registerResult
    }

    override suspend fun login(email: String, password: String): NetworkResult<ResponseAuth> {
        loginCallCount++
        lastLoginEmail = email
        lastLoginPassword = password
        return loginResult
    }

    override suspend fun getUser(userId: String): NetworkResult<User> {
        getUserCallCount++
        return getUserResult
    }

    override suspend fun updateUser(
            userId: String,
            firstname: String?,
            lastname: String?,
            secondname: String?,
            datebirthday: String?,
            gender: String?
    ): NetworkResult<User> {
        updateUserCallCount++
        return updateUserResult
    }

    override suspend fun logout(): NetworkResult<Unit> {
        logoutCallCount++
        return logoutResult
    }
}
