package com.example.lol.data.repository

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseAuth
import com.example.lol.data.network.models.ResponseRegister
import com.example.lol.data.network.models.User

/**
 * Интерфейс репозитория для операций авторизации. Абстрагирует сетевой слой для возможности замены
 * реализации.
 */
interface IAuthRepository {

    /**
     * Регистрация нового пользователя.
     * @param email Email пользователя
     * @param password Пароль
     * @return Результат регистрации
     */
    suspend fun register(email: String, password: String): NetworkResult<ResponseRegister>

    /**
     * Авторизация пользователя.
     * @param email Email пользователя
     * @param password Пароль
     * @return Результат авторизации с токеном
     */
    suspend fun login(email: String, password: String): NetworkResult<ResponseAuth>

    /**
     * Получение информации о пользователе.
     * @param userId ID пользователя
     * @return Данные пользователя
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
    suspend fun logout(): NetworkResult<Unit>
}
