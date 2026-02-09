package com.example.lol.data.network

/**
 * Sealed class для представления результата сетевого запроса. Обеспечивает типобезопасную обработку
 * успеха, ошибки и состояния загрузки.
 */
// Определяет поведение и состояние компонента в рамках текущего модуля.
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
    // Возвращает полезные данные из успешного результата или `null` при другом состоянии.
    fun getOrNull(): T? = (this as? Success)?.data

    /** Получает сообщение об ошибке или null. */
    // Возвращает текст ошибки, если результат находится в состоянии ошибки.
    fun errorMessageOrNull(): String? = (this as? Error)?.message

    /** Преобразует данные в случае успеха. */
    /**
     * Преобразует входные данные в целевой формат результата.
     *
     * @param transform Функция преобразования успешного результата в новый тип.
     */
    inline fun <R> map(transform: (T) -> R): NetworkResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
            is Loading -> this
        }
    }

    /** Выполняет действие в случае успеха. */
    /**
     * Вызывает обработчик только для успешного результата и возвращает исходный `NetworkResult`.
     *
     * @param action Функция-обработчик, вызываемая при выполнении условия.
     */
    inline fun onSuccess(action: (T) -> Unit): NetworkResult<T> {
        if (this is Success) action(data)
        return this
    }

    /** Выполняет действие в случае ошибки. */
    /**
     * Вызывает обработчик только при ошибке и возвращает исходный `NetworkResult`.
     *
     * @param action Функция-обработчик, вызываемая при выполнении условия.
     */
    inline fun onError(action: (String, Int?) -> Unit): NetworkResult<T> {
        if (this is Error) action(message, code)
        return this
    }
}
