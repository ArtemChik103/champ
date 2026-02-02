

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

/**
 * Unit-тесты для OrderRepository с использованием MockWebServer.
 */
class OrderRepositoryTest {
    
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: MatuleApi
    private lateinit var repository: OrderRepository
    
    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MatuleApi::class.java)
        
        repository = OrderRepository(api)
    }
    
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
    
    @Test
    fun `createOrder success returns order data`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestJsonResponses.orderJson)
        )
        
        // When
        val result = repository.createOrder(
            userId = "user123",
            productId = "prod1",
            count = 3
        )
        
        // Then
        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertEquals("order1", data?.id)
        assertEquals("user123", data?.userId)
        assertEquals("prod1", data?.productId)
        assertEquals(3, data?.count)
        
        // Verify request body verification is skipped here as we trust Retrofit,
        // but we could verify recordedRequest if needed.
    }
    
    @Test
    fun `createOrder failure returns error`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(TestJsonResponses.errorJson)
        )
        
        // When
        val result = repository.createOrder(
            userId = "user123",
            productId = "prod1",
            count = 3
        )
        
        // Then
        assertTrue(result.isError)
        assertEquals("Failed to create record.", result.errorMessageOrNull())
    }
}
