package com.example.lol.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Mock validation
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.matches(Regex("[a-zA-Z0-9.@]+"))
    }

    fun isValidPassword(password: String): Boolean {
        // Validation: min 8, uppercase, lowercase, digits, special chars (simplified for now)
        return password.length >= 8 
    }

    fun signIn(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(1500) // Mock network delay
            // Mock success
            if (isValidEmail(email) && isValidPassword(pass)) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Invalid credentials")
            }
        }
    }

    fun signUp(email: String, pass: String) {
         viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(1500)
             if (isValidEmail(email) && isValidPassword(pass)) {
                 _authState.value = AuthState.Success
             } else {
                 _authState.value = AuthState.Error("Invalid input")
             }
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
