package com.example.lol.data.repository

import com.example.lol.data.network.api.MatuleApi
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

/** Unit-тесты для CartRepository с использованием MockWebServer. */
// Содержит набор тестов для проверки поведения соответствующего модуля.
class CartRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: MatuleApi
    private lateinit var repository: CartRepository

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

        repository = CartRepository(api)
    }

    // Освобождает ресурсы и очищает тестовое окружение после выполнения тестов.
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `addToCart success returns cart item`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.cartItemJson)
        )

        // When
        // Когда
        val result = repository.addToCart(userId = "user123", productId = "prod1", count = 2)

        // Then
        // Тогда
        assertTrue(result.isSuccess)
        val cartItem = result.getOrNull()
        assertNotNull(cartItem)
        assertEquals("cart1", cartItem?.id)
        assertEquals("prod1", cartItem?.productId)
        assertEquals(2, cartItem?.count)
    }

    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
    @Test
    fun `addToCart failure returns error`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(400).setBody(TestJsonResponses.errorJson)
        )

        // When
        // Когда
        val result = repository.addToCart(userId = "user123", productId = "invalid", count = 1)

        // Then
        // Тогда
        assertTrue(result.isError)
        assertEquals("Failed to create record.", result.errorMessageOrNull())
    }

    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
    @Test
    fun `updateCartItem network error returns error`() = runTest {
        // Given - shutdown server to simulate network error
        // Дано: останавливаем сервер для имитации сетевой ошибки.
        mockWebServer.shutdown()

        // When
        // Когда
        val result =
                repository.updateCartItem(
                        cartItemId = "cart1",
                        userId = "user123",
                        productId = "prod1",
                        count = 5
                )

        // Then
        // Тогда
        assertTrue(result.isError)
        assertNotNull(result.errorMessageOrNull())
    }
}
