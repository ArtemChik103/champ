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
// Инкапсулирует работу с источниками данных и обработку результатов операций.
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
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param response Ответ сервера, который нужно преобразовать в результат операции.
     */
    fun setRegisterSuccess(response: ResponseRegister) {
        registerResult = NetworkResult.Success(response)
    }

    /** Настраивает ошибку регистрации. */
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setRegisterError(message: String, code: Int? = null) {
        registerResult = NetworkResult.Error(message, code)
    }

    /** Настраивает успешный ответ на авторизацию. */
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param response Ответ сервера, который нужно преобразовать в результат операции.
     */
    fun setLoginSuccess(response: ResponseAuth) {
        loginResult = NetworkResult.Success(response)
    }

    /** Настраивает ошибку авторизации. */
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setLoginError(message: String, code: Int? = null) {
        loginResult = NetworkResult.Error(message, code)
    }

    /** Настраивает успешный ответ получения пользователя. */
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param user Модель пользователя для сохранения или передачи.
     */
    fun setGetUserSuccess(user: User) {
        getUserResult = NetworkResult.Success(user)
    }

    /** Настраивает ошибку получения пользователя. */
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setGetUserError(message: String, code: Int? = null) {
        getUserResult = NetworkResult.Error(message, code)
    }

    /** Настраивает успешный ответ обновления пользователя. */
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param user Модель пользователя для сохранения или передачи.
     */
    fun setUpdateUserSuccess(user: User) {
        updateUserResult = NetworkResult.Success(user)
    }

    /** Настраивает ошибку обновления пользователя. */
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     * @param code Код статуса или ошибки для обработки результата.
     */
    fun setUpdateUserError(message: String, code: Int? = null) {
        updateUserResult = NetworkResult.Error(message, code)
    }

    /** Настраивает ошибку logout. */
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param message Текст сообщения, отображаемый пользователю.
     */
    fun setLogoutError(message: String) {
        logoutResult = NetworkResult.Error(message)
    }

    /** Сброс всех счётчиков и настроек. */
    // Сбрасывает состояние к исходным значениям по умолчанию.
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

    /**
     * Регистрирует сущность и сохраняет результат операции в текущем состоянии.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     * @param password Пароль пользователя для проверки или сохранения.
     */
    override suspend fun register(
            email: String,
            password: String
    ): NetworkResult<ResponseRegister> {
        registerCallCount++
        lastRegisterEmail = email
        return registerResult
    }

    /**
     * Выполняет вход пользователя и обновляет состояние авторизации.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     * @param password Пароль пользователя для проверки или сохранения.
     */
    override suspend fun login(email: String, password: String): NetworkResult<ResponseAuth> {
        loginCallCount++
        lastLoginEmail = email
        lastLoginPassword = password
        return loginResult
    }

    /**
     * Возвращает актуальные данные из текущего источника состояния.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     */
    override suspend fun getUser(userId: String): NetworkResult<User> {
        getUserCallCount++
        return getUserResult
    }

    /**
     * Обновляет существующую сущность и возвращает результат операции.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     * @param firstname Имя пользователя для сохранения или обновления профиля.
     * @param lastname Фамилия пользователя для сохранения или обновления профиля.
     * @param secondname Отчество пользователя для заполнения профиля.
     * @param datebirthday Дата рождения в формате, ожидаемом сервером.
     * @param gender Выбранный пол пользователя или проекта.
     */
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

    // Завершает пользовательскую сессию и очищает данные авторизации.
    override suspend fun logout(): NetworkResult<Unit> {
        logoutCallCount++
        return logoutResult
    }
}
