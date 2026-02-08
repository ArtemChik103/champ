package com.example.lol.domain.usecase.auth

import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ResponseRegister
import com.example.lol.data.repository.IAuthRepository

class RegisterUseCase(private val authRepository: IAuthRepository) {
    suspend operator fun invoke(email: String, password: String): NetworkResult<ResponseRegister> {
        return authRepository.register(email, password)
    }
}
