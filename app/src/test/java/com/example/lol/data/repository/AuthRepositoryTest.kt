package com.example.lol.data.repository

import com.example.lol.data.network.api.MatuleApi
import com.example.lol.data.network.models.*
import com.example.lol.testdata.TestJsonResponses
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Unit-тесты для AuthRepository с использованием MockWebServer. Использует StubTokenManager для
 * изоляции от Android Context.
 */
// Содержит набор тестов для проверки поведения соответствующего модуля.
class AuthRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: MatuleApi
    private lateinit var repository: AuthRepository
    private lateinit var stubTokenManager: StubTokenManager

    // Подготавливает тестовое окружение и зависимости перед запуском тестов.
    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api =
                Retrofit.Builder()
                        .baseUrl(mockWebServer.url("/"))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(MatuleApi::class.java)

        stubTokenManager = StubTokenManager()
        repository = AuthRepository(api, stubTokenManager)
    }

    // Освобождает ресурсы и очищает тестовое окружение после выполнения тестов.
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `login success returns token and user`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.successLoginJson)
        )

        // When
        // Когда
        val result = repository.login("test@test.ru", "password123")

        // Then
        // Тогда
        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test", data?.token)
        assertEquals("user123", data?.record?.id)
        assertEquals("Иван", data?.record?.firstname)

        // Verify token was saved
        // Проверяем, что токен сохранён.
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test", stubTokenManager.savedToken)
        assertEquals("user123", stubTokenManager.savedUserId)
    }

    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
    @Test
    fun `login failure returns error`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse()
                        .setResponseCode(400)
                        .setBody(TestJsonResponses.invalidCredentialsJson)
        )

        // When
        // Когда
        val result = repository.login("wrong@test.ru", "wrongpassword")

        // Then
        // Тогда
        assertTrue(result.isError)
        assertEquals("Invalid credentials.", result.errorMessageOrNull())
    }

    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `register success returns user data`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.successRegisterJson)
        )

        // When
        // Когда
        val result = repository.register("new@test.ru", "password123")

        // Then
        // Тогда
        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("user456", data?.id)
        assertFalse(data?.verified ?: true)
    }

    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
    @Test
    fun `register failure returns error`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(400).setBody(TestJsonResponses.errorJson)
        )

        // When
        // Когда
        val result = repository.register("existing@test.ru", "password123")

        // Then
        // Тогда
        assertTrue(result.isError)
        assertEquals("Failed to create record.", result.errorMessageOrNull())
    }

    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `updateUser success returns updated user`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.updatedUserJson)
        )
        stubTokenManager.savedUserId = "user123"

        // When
        // Когда
        val result = repository.updateUser(
                userId = "user123",
                firstname = "Петр",
                lastname = "Петров",
                secondname = "Петрович",
                datebirthday = "1995-05-15",
                gender = "Мужской"
        )

        // Then
        // Тогда
        assertTrue(result.isSuccess)
        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals("Петр", user?.firstname)
        assertEquals("Петров", user?.lastname)
    }

    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `logout success clears tokens`() = runTest {
        // Given
        // Дано
        stubTokenManager.savedToken = "test_token"
        stubTokenManager.savedUserId = "user123"
        
        // First response for getUsersAuth
        // Первый ответ для getUsersAuth.
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.usersAuthJson)
        )
        // Second response for logout (delete token)
        // Второй ответ для logout (удаление токена).
        mockWebServer.enqueue(
                MockResponse().setResponseCode(204)
        )

        // When
        // Когда
        val result = repository.logout()

        // Then
        // Тогда
        assertTrue(result.isSuccess)
        assertNull(stubTokenManager.savedToken)
        assertNull(stubTokenManager.savedUserId)
    }

    // Ожидаемый результат: поведение в тестовом сценарии соответствует ожидаемому результату.
    @Test
    fun `logout when token missing still clears local auth`() = runTest {
        stubTokenManager.savedToken = "test_token"
        stubTokenManager.savedUserId = "unknown_user"

        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.usersAuthJson)
        )

        val result = repository.logout()

        assertTrue(result.isSuccess)
        assertNull(stubTokenManager.savedToken)
        assertNull(stubTokenManager.savedUserId)
    }
}

/** Stub реализация для тестов без Android Context. Имитирует поведение TokenManager в памяти. */
// Управляет локальными данными и настройками, связанными с пользовательской сессией.
class StubTokenManager : com.example.lol.data.network.ITokenManager {
    var savedToken: String? = null
    var savedUserId: String? = null

    /**
     * Сохраняет переданные данные в целевое хранилище.
     *
     * @param token Токен авторизации для выполнения защищенных запросов.
     */
    override fun saveToken(token: String) {
        savedToken = token
    }

    // Возвращает актуальные данные из текущего источника состояния.
    override fun getToken(): String? = savedToken

    /**
     * Сохраняет переданные данные в целевое хранилище.
     *
     * @param userId Идентификатор пользователя, от имени которого выполняется операция.
     */
    override fun saveUserId(userId: String) {
        savedUserId = userId
    }

    // Возвращает актуальные данные из текущего источника состояния.
    override fun getUserId(): String? = savedUserId

    // Проверяет наличие данных или состояния и возвращает булев результат.
    override fun hasToken(): Boolean = !savedToken.isNullOrBlank()

    // Очищает связанные данные и приводит состояние к пустому виду.
    override fun clearAuth() {
        savedToken = null
        savedUserId = null
    }
}
