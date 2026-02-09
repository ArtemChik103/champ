package com.example.lol.authorization

import android.util.Log
import com.example.lol.data.network.ITokenManager
import com.example.lol.data.network.RetrofitInstance
import com.example.lol.data.repository.AuthRepository
import com.example.lol.data.repository.IAuthRepository
import com.example.lol.data.repository.MockAuthRepository

// Содержит конфигурационные константы для соответствующего сценария.
object AuthRuntimeConfig {
    const val useMockAuthInRuntime: Boolean = true
}

// Предоставляет зависимости и выбирает реализацию по текущему режиму работы.
object AuthRepositoryProvider {

    /**
     * Создает и возвращает реализацию, подходящую для текущего режима.
     *
     * @param tokenManager Компонент хранения и чтения токена авторизации.
     */
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

    // Возвращает текстовое название активного режима авторизации.
    fun modeLabel(): String {
        return if (AuthRuntimeConfig.useMockAuthInRuntime) {
            "MockAuth mode"
        } else {
            "Network auth mode"
        }
    }

    // Проверяет условие и возвращает `true`, если оно выполняется.
    fun isMockMode(): Boolean = AuthRuntimeConfig.useMockAuthInRuntime
}