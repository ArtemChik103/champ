package com.example.lol.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.TokenManager
import com.example.lol.domain.usecase.auth.LoginUseCase
import com.example.lol.domain.usecase.auth.LogoutUseCase
import com.example.lol.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel для авторизации и регистрации пользователей. Использует UseCases для бизнес-логики и
 * SessionManager для локального кэша.
 */
// Хранит состояние экрана и координирует действия пользователя.
class AuthViewModel(
        private val loginUseCase: LoginUseCase,
        private val registerUseCase: RegisterUseCase,
        private val logoutUseCase: LogoutUseCase,
        private val tokenManager: TokenManager,
        private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /** Валидация email по паттерну name@domenname.ru (только строчные буквы и цифры). */
    /**
     * Проверяет условие и возвращает `true`, если оно выполняется.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     */
    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]+$"))
    }

    /**
     * Валидация пароля:
     * - Не менее 8 символов
     * - Заглавные и строчные буквы
     * - Цифры
     * - Спецсимволы
     */
    /**
     * Проверяет условие и возвращает `true`, если оно выполняется.
     *
     * @param password Пароль пользователя для проверки или сохранения.
     */
    fun isValidPassword(password: String): Boolean {
        val hasLength = password.length >= 8
        val hasCase = password.any { it.isLowerCase() } && password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() && !it.isWhitespace() }
        return hasLength && hasCase && hasDigit && hasSpecial
    }

    /** Проверка наличия пользователя в локальном кэше. */
    /**
     * Проверяет наличие пользователя с указанным email в локальном списке аккаунтов.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     */
    fun checkUserExists(email: String): Boolean {
        return sessionManager.isUserExists(email)
    }

    /**
     * Вход пользователя через API. При успехе токен сохраняется в TokenManager автоматически (через
     * Repository, вызываемый UseCase).
     */
    /**
     * Выполняет вход пользователя и обновляет состояние авторизации.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     * @param password Пароль пользователя для проверки или сохранения.
     */
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true

            when (val result = loginUseCase(email, password)) {
                is NetworkResult.Success -> {
                    // Сохраняем данные в локальный кэш для совместимости
                    sessionManager.setCurrentEmail(email)
                    sessionManager.setLoggedIn(true)

                    // Сохраняем в SessionManager если пользователь новый
                    // Сохраняем в SessionManager, если пользователь новый.
                    if (!sessionManager.isUserExists(email)) {
                        val name = result.data.record.firstname.ifBlank { "User" }
                        sessionManager.registerUser(email, name)
                    }

                    _authState.value = AuthState.Success
                }
                is NetworkResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
                is NetworkResult.Loading -> {
                    // Уже обрабатывается выше
                }
            }
            _isLoading.value = false
        }
    }

    /** Регистрация нового пользователя через API. */
    /**
     * Запускает регистрацию через выбранный репозиторий и обновляет состояние авторизации.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     * @param name Имя пользователя или название сущности.
     * @param password Пароль пользователя для проверки или сохранения.
     */
    fun signUp(email: String, name: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true

            when (val result = registerUseCase(email, password)) {
                is NetworkResult.Success -> {
                    // Сохраняем данные локально
                    sessionManager.registerUser(email, name)
                    sessionManager.setCurrentEmail(email)
                    sessionManager.savePassword(email, password)

                    _authState.value = AuthState.Success
                }
                is NetworkResult.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
                is NetworkResult.Loading -> {
                    // Уже обрабатывается выше
                }
            }
            _isLoading.value = false
        }
    }

    /**
     * Регистрация без API (для обратной совместимости). Используется когда нужна только локальная
     * регистрация.
     */
    /**
     * Регистрирует пользователя локально и переводит состояние в успешное.
     *
     * @param email Email пользователя, используемый как идентификатор учетной записи.
     * @param name Имя пользователя или название сущности.
     */
    fun signUpLocal(email: String, name: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true

            if (sessionManager.isUserExists(email)) {
                _authState.value = AuthState.Error("Аккаунт уже существует")
            } else {
                sessionManager.registerUser(email, name)
                sessionManager.setCurrentEmail(email)
                _authState.value = AuthState.Success
            }
            _isLoading.value = false
        }
    }

    /** Выход из системы. */
    // Завершает пользовательскую сессию и очищает данные авторизации.
    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true

            logoutUseCase()
            sessionManager.clearSession()
            tokenManager.clearAuth()

            _authState.value = AuthState.Idle
            _isLoading.value = false
        }
    }

    /** Сброс состояния авторизации. */
    // Сбрасывает состояние к исходным значениям по умолчанию.
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

/** Factory для создания AuthViewModel с зависимостями. */
// Создает экземпляры компонентов с необходимыми зависимостями.
class AuthViewModelFactory(
        private val loginUseCase: LoginUseCase,
        private val registerUseCase: RegisterUseCase,
        private val logoutUseCase: LogoutUseCase,
        private val tokenManager: TokenManager,
        private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param modelClass Класс ViewModel, для которого создается экземпляр.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(
                loginUseCase,
                registerUseCase,
                logoutUseCase,
                tokenManager,
                sessionManager
        ) as
                T
    }
}

/** Состояния авторизации. */
// Определяет поведение и состояние компонента в рамках текущего модуля.
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
