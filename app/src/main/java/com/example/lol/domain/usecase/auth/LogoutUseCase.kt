package com.example.lol.domain.usecase.auth

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.repository.IAuthRepository

// Определяет поведение и состояние компонента в рамках текущего модуля.
class LogoutUseCase(private val authRepository: IAuthRepository) {
    // Запускает переданный обработчик и возвращает результат его выполнения.
    suspend operator fun invoke(): NetworkResult<Unit> {
        return authRepository.logout()
    }
}
