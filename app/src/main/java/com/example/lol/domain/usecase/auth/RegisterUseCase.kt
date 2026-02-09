package com.example.lol.domain.usecase.auth

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseRegister
import com.example.lol.data.repository.IAuthRepository

// Определяет поведение и состояние компонента в рамках текущего модуля.
class RegisterUseCase(private val authRepository: IAuthRepository) {
    /**
     * Запускает переданный обработчик и возвращает результат его выполнения.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     * @param password Пароль пользователя для проверки или сохранения.
     */
    suspend operator fun invoke(email: String, password: String): NetworkResult<ResponseRegister> {
        return authRepository.register(email, password)
    }
}
