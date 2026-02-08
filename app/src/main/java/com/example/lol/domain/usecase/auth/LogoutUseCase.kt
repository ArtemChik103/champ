package com.example.lol.domain.usecase.auth

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.repository.IAuthRepository

class LogoutUseCase(private val authRepository: IAuthRepository) {
    suspend operator fun invoke(): NetworkResult<Unit> {
        return authRepository.logout()
    }
}
