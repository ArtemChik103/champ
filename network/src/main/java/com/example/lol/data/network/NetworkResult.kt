package com.example.lol.data.network

/**
 * Sealed class для представления результата сетевого запроса. Обеспечивает типобезопасную обработку
 * успеха, ошибки и состояния загрузки.
 */
sealed class NetworkResult<out T> {

    /**
     * Успешный результат с данными.
     * @param data Данные полученные от сервера
     */
    data class Success<T>(val data: T) : NetworkResult<T>()

    /**
     * Ошибка выполнения запроса.
     * @param message Сообщение об ошибке
     * @param code HTTP код ошибки (опционально)
     */
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()

    /** Состояние загрузки. */
    object Loading : NetworkResult<Nothing>()

    /** Проверяет успешность результата. */
    val isSuccess: Boolean
        get() = this is Success

    /** Проверяет наличие ошибки. */
    val isError: Boolean
        get() = this is Error

    /** Проверяет состояние загрузки. */
    val isLoading: Boolean
        get() = this is Loading

    /** Получает данные или null если результат не Success. */
    fun getOrNull(): T? = (this as? Success)?.data

    /** Получает сообщение об ошибке или null. */
    fun errorMessageOrNull(): String? = (this as? Error)?.message

    /** Преобразует данные в случае успеха. */
    inline fun <R> map(transform: (T) -> R): NetworkResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
            is Loading -> this
        }
    }

    /** Выполняет действие в случае успеха. */
    inline fun onSuccess(action: (T) -> Unit): NetworkResult<T> {
        if (this is Success) action(data)
        return this
    }

    /** Выполняет действие в случае ошибки. */
    inline fun onError(action: (String, Int?) -> Unit): NetworkResult<T> {
        if (this is Error) action(message, code)
        return this
    }
}
