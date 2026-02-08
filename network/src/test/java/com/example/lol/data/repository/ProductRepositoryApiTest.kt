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
class ProductRepositoryApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: MatuleApi
    private lateinit var repository: ProductRepositoryApi

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

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getProducts success returns product list`() = runTest {
        // Given
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.productsListJson)
        )

        // When
        val result = repository.getProducts()

        // Then
        assertTrue(result.isSuccess)
        val products = result.getOrNull()
        assertNotNull(products)
        assertEquals(2, products?.size)
        assertEquals("Футболка", products?.first()?.title)
        assertEquals(1500, products?.first()?.price)
    }

    @Test
    fun `getProductById success returns product details`() = runTest {
        // Given
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.productDetailJson)
        )

        // When
        val result = repository.getProductById("prod1")

        // Then
        assertTrue(result.isSuccess)
        val product = result.getOrNull()
        assertNotNull(product)
        assertEquals("prod1", product?.id)
        assertEquals("Футболка", product?.title)
        assertEquals("Хлопковая футболка", product?.description)
    }

    @Test
    fun `searchProducts sends correct filter parameter`() = runTest {
        // Given
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.productsListJson)
        )

        // When
        val result = repository.searchProducts("Футболка")

        // Then
        assertTrue(result.isSuccess)

        // Verify the request was made with correct filter
        val request = mockWebServer.takeRequest()
        assertTrue(request.path?.contains("filter") == true)
    }

    @Test
    fun `getProducts failure returns error`() = runTest {
        // Given
        mockWebServer.enqueue(
                MockResponse().setResponseCode(400).setBody(TestJsonResponses.errorJson)
        )

        // When
        val result = repository.getProducts()

        // Then
        assertTrue(result.isError)
        assertNotNull(result.errorMessageOrNull())
    }

    @Test
    fun `getNews success returns news list`() = runTest {
        mockWebServer.enqueue(
                MockResponse().setResponseCode(200).setBody(TestJsonResponses.newsListJson)
        )

        val result = repository.getNews()

        assertTrue(result.isSuccess)
        val news = result.getOrNull()
        assertNotNull(news)
        assertEquals(1, news?.size)
        assertEquals("news1", news?.first()?.id)
    }

    @Test
    fun `getNews failure returns server message`() = runTest {
        mockWebServer.enqueue(
                MockResponse().setResponseCode(400).setBody(TestJsonResponses.errorJson)
        )

        val result = repository.getNews()

        assertTrue(result.isError)
        assertEquals("Failed to create record.", result.errorMessageOrNull())
    }
}
