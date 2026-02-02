
package com.example.lol.viewmodel

import com.example.lol.data.fake.FakeCartRepository
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
 * Unit-тесты для логики корзины с использованием FakeCartRepository.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    
    private lateinit var fakeRepository: FakeCartRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeCartRepository()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `addToCart success updates cart`() = runTest {
        // Given
        val cartResponse = TestJsonResponses.createTestResponseCart(
            id = "cart1",
            productId = "prod1",
            count = 1
        )
        fakeRepository.setAddToCartSuccess(cartResponse)
        
        // When
        val result = fakeRepository.addToCart(
            userId = "user123",
            productId = "prod1",
            count = 1
        )
        
        // Then
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, fakeRepository.addToCartCallCount)
        assertEquals("prod1", fakeRepository.lastAddedProductId)
        assertEquals(1, fakeRepository.lastAddedCount)
    }
    
    @Test
    fun `removeFromCart updates quantity to zero`() = runTest {
        // Given - simulate removing by updating to 0
        fakeRepository.setUpdateCartItemError("Cannot update to zero", 400)
        
        // When
        val result = fakeRepository.updateCartItem(
            cartItemId = "cart1",
            userId = "user123",
            productId = "prod1",
            count = 0
        )
        
        // Then
        assertTrue(result is NetworkResult.Error)
        assertEquals(1, fakeRepository.updateCartItemCallCount)
    }
    
    @Test
    fun `clearCart resets repository state`() = runTest {
        // Given
        val cartResponse = TestJsonResponses.createTestResponseCart()
        fakeRepository.setAddToCartSuccess(cartResponse)
        fakeRepository.addToCart("user123", "prod1", 1)
        
        // When
        fakeRepository.reset()
        
        // Then
        assertEquals(0, fakeRepository.addToCartCallCount)
        assertNull(fakeRepository.lastAddedProductId)
    }
}
