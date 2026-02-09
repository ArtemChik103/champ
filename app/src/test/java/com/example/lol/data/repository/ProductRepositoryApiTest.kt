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

/** Unit-тесты для ProductRepositoryApi с использованием MockWebServer. */
// Содержит набор тестов для проверки поведения соответствующего модуля.
class ProductRepositoryApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: MatuleApi
    private lateinit var repository: ProductRepositoryApi

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

        repository = ProductRepositoryApi(api)
    }

    // Освобождает ресурсы и очищает тестовое окружение после выполнения тестов.
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `getProducts success returns product list`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.productsListJson)
        )

        // When
        // Когда
        val result = repository.getProducts()

        // Then
        // Тогда
        assertTrue(result.isSuccess)
        val products = result.getOrNull()
        assertNotNull(products)
        assertEquals(2, products?.size)
        assertEquals("Футболка", products?.first()?.title)
        assertEquals(1500, products?.first()?.price)
    }

    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `getProductById success returns product details`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.productDetailJson)
        )

        // When
        // Когда
        val result = repository.getProductById("prod1")

        // Then
        // Тогда
        assertTrue(result.isSuccess)
        val product = result.getOrNull()
        assertNotNull(product)
        assertEquals("prod1", product?.id)
        assertEquals("Футболка", product?.title)
        assertEquals("Хлопковая футболка", product?.description)
    }

    // Ожидаемый результат: фактический результат совпадает с ожидаемым значением.
    @Test
    fun `searchProducts sends correct filter parameter`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.productsListJson)
        )

        // When
        // Когда
        val result = repository.searchProducts("Футболка")

        // Then
        // Тогда
        assertTrue(result.isSuccess)

        // Verify the request was made with correct filter
        // Проверяем, что запрос отправлен с корректным фильтром.
        val request = mockWebServer.takeRequest()
        assertTrue(request.path?.contains("filter") == true)
    }

    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
    @Test
    fun `getProducts failure returns error`() = runTest {
        // Given
        // Дано
        mockWebServer.enqueue(
                MockResponse().setResponseCode(400).setBody(TestJsonResponses.errorJson)
        )

        // When
        // Когда
        val result = repository.getProducts()

        // Then
        // Тогда
        assertTrue(result.isError)
        assertNotNull(result.errorMessageOrNull())
    }
}
