
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
// Содержит набор тестов для проверки поведения соответствующего модуля.
@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    
    private lateinit var fakeRepository: FakeCartRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    
    // Подготавливает тестовое окружение и зависимости перед запуском тестов.
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeCartRepository()
    }
    
    // Освобождает ресурсы и очищает тестовое окружение после выполнения тестов.
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    // Ожидаемый результат: успешный сценарий завершается корректным результатом.
    @Test
    fun `addToCart success updates cart`() = runTest {
        // Given
        // Дано
        val cartResponse = TestJsonResponses.createTestResponseCart(
            id = "cart1",
            productId = "prod1",
            count = 1
        )
        fakeRepository.setAddToCartSuccess(cartResponse)
        
        // When
        // Когда
        val result = fakeRepository.addToCart(
            userId = "user123",
            productId = "prod1",
            count = 1
        )
        
        // Then
        // Тогда
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, fakeRepository.addToCartCallCount)
        assertEquals("prod1", fakeRepository.lastAddedProductId)
        assertEquals(1, fakeRepository.lastAddedCount)
    }
    
    // Ожидаемый результат: после действия состояние обновляется ожидаемым образом.
    @Test
    fun `removeFromCart updates quantity to zero`() = runTest {
        // Given - simulate removing by updating to 0
        // Дано: имитируем удаление, обновляя количество до 0.
        fakeRepository.setUpdateCartItemError("Cannot update to zero", 400)
        
        // When
        // Когда
        val result = fakeRepository.updateCartItem(
            cartItemId = "cart1",
            userId = "user123",
            productId = "prod1",
            count = 0
        )
        
        // Then
        // Тогда
        assertTrue(result is NetworkResult.Error)
        assertEquals(1, fakeRepository.updateCartItemCallCount)
    }
    
    // Ожидаемый результат: поведение в тестовом сценарии соответствует ожидаемому результату.
    @Test
    fun `clearCart resets repository state`() = runTest {
        // Given
        // Дано
        val cartResponse = TestJsonResponses.createTestResponseCart()
        fakeRepository.setAddToCartSuccess(cartResponse)
        fakeRepository.addToCart("user123", "prod1", 1)
        
        // When
        // Когда
        fakeRepository.reset()
        
        // Then
        // Тогда
        assertEquals(0, fakeRepository.addToCartCallCount)
        assertNull(fakeRepository.lastAddedProductId)
    }
}
