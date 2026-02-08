package com.example.lol.domain.usecase.auth

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseAuth
import com.example.lol.data.repository.IAuthRepository

class LoginUseCase(private val authRepository: IAuthRepository) {
    suspend operator fun invoke(email: String, password: String): NetworkResult<ResponseAuth> {
        return authRepository.login(email, password)
    }
}
