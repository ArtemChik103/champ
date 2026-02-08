package com.example.lol.authorization

import android.util.Log
import com.example.lol.data.network.ITokenManager
import com.example.lol.data.network.RetrofitInstance
import com.example.lol.data.repository.AuthRepository
import com.example.lol.data.repository.IAuthRepository
import com.example.lol.data.repository.MockAuthRepository

object AuthRuntimeConfig {
    const val useMockAuthInRuntime: Boolean = true
}

object AuthRepositoryProvider {

    fun provide(tokenManager: ITokenManager): IAuthRepository {
        val repository =
            if (AuthRuntimeConfig.useMockAuthInRuntime) {
                MockAuthRepository.instance
            } else {
                AuthRepository(RetrofitInstance.api, tokenManager)
            }

        Log.i("AuthRuntime", "Auth repository mode: ${modeLabel()}")
        return repository
    }

    fun modeLabel(): String {
        return if (AuthRuntimeConfig.useMockAuthInRuntime) {
            "MockAuth mode"
        } else {
            "Network auth mode"
        }
    }

    fun isMockMode(): Boolean = AuthRuntimeConfig.useMockAuthInRuntime
}