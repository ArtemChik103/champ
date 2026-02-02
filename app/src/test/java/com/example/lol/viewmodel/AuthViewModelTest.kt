
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
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    
    private lateinit var fakeRepository: FakeAuthRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeAuthRepository()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `login with valid credentials calls repository`() = runTest {
        // Given
        val authResponse = TestJsonResponses.createTestResponseAuth()
        fakeRepository.setLoginSuccess(authResponse)
        
        // When
        val result = fakeRepository.login("test@test.ru", "password123")
        
        // Then
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, fakeRepository.loginCallCount)
        assertEquals("test@test.ru", fakeRepository.lastLoginEmail)
        assertEquals("password123", fakeRepository.lastLoginPassword)
    }
    
    @Test
    fun `login with invalid credentials returns error`() = runTest {
        // Given
        fakeRepository.setLoginError("Invalid credentials.", 400)
        
        // When
        val result = fakeRepository.login("wrong@test.ru", "wrongpass")
        
        // Then
        assertTrue(result is NetworkResult.Error)
        assertEquals("Invalid credentials.", (result as NetworkResult.Error).message)
        assertEquals(400, result.code)
    }
    
    @Test
    fun `register with new email calls repository`() = runTest {
        // Given
        val registerResponse = TestJsonResponses.createTestResponseRegister()
        fakeRepository.setRegisterSuccess(registerResponse)
        
        // When
        val result = fakeRepository.register("new@test.ru", "password123")
        
        // Then
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, fakeRepository.registerCallCount)
        assertEquals("new@test.ru", fakeRepository.lastRegisterEmail)
    }
    
    @Test
    fun `register with existing email returns error`() = runTest {
        // Given
        fakeRepository.setRegisterError("Email already exists", 400)
        
        // When
        val result = fakeRepository.register("existing@test.ru", "password123")
        
        // Then
        assertTrue(result is NetworkResult.Error)
        assertEquals("Email already exists", (result as NetworkResult.Error).message)
    }
}
