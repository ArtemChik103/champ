package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseAuth
import com.example.lol.data.network.models.ResponseRegister
import com.example.lol.data.network.models.User

/**
 * Интерфейс репозитория для операций авторизации. Абстрагирует сетевой слой для возможности замены
 * реализации.
 */
// Определяет контракт репозитория для операций с доменными данными.
interface IAuthRepository {

    /**
     * Регистрация нового пользователя.
     * @param email Email пользователя
     * @param password Пароль
     * @return Результат регистрации
     */
    /**
     * Регистрирует сущность и сохраняет результат операции в текущем состоянии.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     * @param password Пароль пользователя для проверки или сохранения.
     */
    suspend fun register(email: String, password: String): NetworkResult<ResponseRegister>

    /**
     * Авторизация пользователя.
     * @param email Email пользователя
     * @param password Пароль
     * @return Результат авторизации с токеном
     */
    /**
     * Выполняет вход пользователя и обновляет состояние авторизации.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     * @param password Пароль пользователя для проверки или сохранения.
     */
    suspend fun login(email: String, password: String): NetworkResult<ResponseAuth>

    /**
     * Получение информации о пользователе.
     * @param userId ID пользователя
     * @return Данные пользователя
     */
    /**
     * Возвращает актуальные данные из текущего источника состояния.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     */
    suspend fun getUser(userId: String): NetworkResult<User>

    /**
     * Обновление профиля пользователя.
     * @param userId ID пользователя
     * @param firstname Имя
     * @param lastname Фамилия
     * @param secondname Отчество
     * @param datebirthday Дата рождения
     * @param gender Пол
     * @return Обновлённые данные пользователя
     */
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
    suspend fun updateUser(
            userId: String,
            firstname: String? = null,
            lastname: String? = null,
            secondname: String? = null,
            datebirthday: String? = null,
            gender: String? = null
    ): NetworkResult<User>

    /**
     * Выход из системы.
     * @return Результат операции
     */
    // Завершает пользовательскую сессию и очищает данные авторизации.
    suspend fun logout(): NetworkResult<Unit>
}
