package com.example.lol.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Mock validation
    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]+$"))
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    fun checkUserExists(email: String): Boolean {
        return sessionManager.isUserExists(email)
    }

    fun signIn(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(1000)

            if (!sessionManager.isUserExists(email)) {
                _authState.value = AuthState.Error("Пользователь не найден")
                return@launch
            }

            if (sessionManager.validatePassword(email, pass)) {
                sessionManager.setCurrentEmail(email)
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Неверный пароль")
            }
        }
    }

    fun signUp(email: String, name: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(1000)
            if (sessionManager.isUserExists(email)) {
                _authState.value = AuthState.Error("Аккаунт уже существует")
            } else {
                sessionManager.registerUser(email, name)
                sessionManager.setCurrentEmail(email)
                _authState.value = AuthState.Success
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

class AuthViewModelFactory(private val sessionManager: SessionManager) :
        androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return AuthViewModel(sessionManager) as T
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
