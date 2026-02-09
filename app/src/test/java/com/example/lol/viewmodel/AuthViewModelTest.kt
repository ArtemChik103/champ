
package com.example.lol.viewmodel

import com.example.lol.data.fake.FakeAuthRepository
import com.example.lol.data.network.NetworkResult
import com.example.lol.testdata.TestJsonResponses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit-тесты для AuthViewModel с использованием FakeAuthRepository.
 * 
 * Примечание: Эти тесты демонстрируют паттерн тестирования ViewModel.
 * Для полноценного тестирования потребуется рефакторинг AuthViewModel
 * для использования IAuthRepository вместо SessionManager.
 */
// Содержит набор тестов для проверки поведения соответствующего модуля.
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    
    private lateinit var fakeRepository: FakeAuthRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    
    // Подготавливает тестовое окружение и зависимости перед запуском тестов.
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeAuthRepository()
    }
    
    // Освобождает ресурсы и очищает тестовое окружение после выполнения тестов.
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    // Ожидаемый результат: поведение в тестовом сценарии соответствует ожидаемому результату.
    @Test
    fun `login with valid credentials calls repository`() = runTest {
        // Given
        // Дано
        val authResponse = TestJsonResponses.createTestResponseAuth()
        fakeRepository.setLoginSuccess(authResponse)
        
        // When
        // Когда
        val result = fakeRepository.login("test@test.ru", "password123")
        
        // Then
        // Тогда
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, fakeRepository.loginCallCount)
        assertEquals("test@test.ru", fakeRepository.lastLoginEmail)
        assertEquals("password123", fakeRepository.lastLoginPassword)
    }
    
    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
    @Test
    fun `login with invalid credentials returns error`() = runTest {
        // Given
        // Дано
        fakeRepository.setLoginError("Invalid credentials.", 400)
        
        // When
        // Когда
        val result = fakeRepository.login("wrong@test.ru", "wrongpass")
        
        // Then
        // Тогда
        assertTrue(result is NetworkResult.Error)
        assertEquals("Invalid credentials.", (result as NetworkResult.Error).message)
        assertEquals(400, result.code)
    }
    
    // Ожидаемый результат: поведение в тестовом сценарии соответствует ожидаемому результату.
    @Test
    fun `register with new email calls repository`() = runTest {
        // Given
        // Дано
        val registerResponse = TestJsonResponses.createTestResponseRegister()
        fakeRepository.setRegisterSuccess(registerResponse)
        
        // When
        // Когда
        val result = fakeRepository.register("new@test.ru", "password123")
        
        // Then
        // Тогда
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, fakeRepository.registerCallCount)
        assertEquals("new@test.ru", fakeRepository.lastRegisterEmail)
    }
    
    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
    @Test
    fun `register with existing email returns error`() = runTest {
        // Given
        // Дано
        fakeRepository.setRegisterError("Email already exists", 400)
        
        // When
        // Когда
        val result = fakeRepository.register("existing@test.ru", "password123")
        
        // Then
        // Тогда
        assertTrue(result is NetworkResult.Error)
        assertEquals("Email already exists", (result as NetworkResult.Error).message)
    }
}
