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
class AuthRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: MatuleApi
    private lateinit var repository: AuthRepository
    private lateinit var stubTokenManager: StubTokenManager

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

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `login success returns token and user`() = runTest {
        // Given
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.successLoginJson)
        )

        // When
        val result = repository.login("test@test.ru", "password123")

        // Then
        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test", data?.token)
        assertEquals("user123", data?.record?.id)
        assertEquals("Иван", data?.record?.firstname)

        // Verify token was saved
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test", stubTokenManager.savedToken)
        assertEquals("user123", stubTokenManager.savedUserId)
    }

    @Test
    fun `login failure returns error`() = runTest {
        // Given
        mockWebServer.enqueue(
                MockResponse()
                        .setResponseCode(400)
                        .setBody(TestJsonResponses.invalidCredentialsJson)
        )

        // When
        val result = repository.login("wrong@test.ru", "wrongpassword")

        // Then
        assertTrue(result.isError)
        assertEquals("Invalid credentials.", result.errorMessageOrNull())
    }

    @Test
    fun `register success returns user data`() = runTest {
        // Given
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.successRegisterJson)
        )

        // When
        val result = repository.register("new@test.ru", "password123")

        // Then
        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("user456", data?.id)
        assertFalse(data?.verified ?: true)
    }

    @Test
    fun `register failure returns error`() = runTest {
        // Given
        mockWebServer.enqueue(
                MockResponse().setResponseCode(400).setBody(TestJsonResponses.errorJson)
        )

        // When
        val result = repository.register("existing@test.ru", "password123")

        // Then
        assertTrue(result.isError)
        assertEquals("Failed to create record.", result.errorMessageOrNull())
    }
}

/** Stub реализация для тестов без Android Context. Имитирует поведение TokenManager в памяти. */
class StubTokenManager : com.example.lol.data.network.ITokenManager {
    var savedToken: String? = null
    var savedUserId: String? = null

    override fun saveToken(token: String) {
        savedToken = token
    }

    override fun getToken(): String? = savedToken

    override fun saveUserId(userId: String) {
        savedUserId = userId
    }

    override fun getUserId(): String? = savedUserId

    override fun hasToken(): Boolean = !savedToken.isNullOrBlank()

    override fun clearAuth() {
        savedToken = null
        savedUserId = null
    }
}
    @Test
    fun `updateUser success returns updated user`() = runTest {
        // Given
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.updatedUserJson)
        )
        stubTokenManager.savedUserId = "user123"

        // When
        val result = repository.updateUser(
                userId = "user123",
                firstname = "Петр",
                lastname = "Петров",
                secondname = "Петрович",
                datebirthday = "1995-05-15",
                gender = "Мужской"
        )

        // Then
        assertTrue(result.isSuccess)
        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals("Петр", user?.firstname)
        assertEquals("Петров", user?.lastname)
    }

    @Test
    fun `logout success clears tokens`() = runTest {
        // Given
        stubTokenManager.savedToken = "test_token"
        stubTokenManager.savedUserId = "user123"
        
        // First response for getUsersAuth
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.usersAuthJson)
        )
        // Second response for logout (delete token)
        mockWebServer.enqueue(
                MockResponse().setResponseCode(204)
        )

        // When
        val result = repository.logout()

        // Then
        assertTrue(result.isSuccess)
        assertNull(stubTokenManager.savedToken)
        assertNull(stubTokenManager.savedUserId)
    }
}

/** Stub реализация для тестов без Android Context. Имитирует поведение TokenManager в памяти. */
class StubTokenManager : com.example.lol.data.network.ITokenManager {
    var savedToken: String? = null
    var savedUserId: String? = null

    override fun saveToken(token: String) {
        savedToken = token
    }

    override fun getToken(): String? = savedToken

    override fun saveUserId(userId: String) {
        savedUserId = userId
    }

    override fun getUserId(): String? = savedUserId

    override fun hasToken(): Boolean = !savedToken.isNullOrBlank()

    override fun clearAuth() {
        savedToken = null
        savedUserId = null
    }
}
