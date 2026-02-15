package com.example.lol.viewmodel

import android.app.Application
import com.example.lol.Catalogue.CatalogueState
import com.example.lol.Catalogue.CatalogueViewModel
import com.example.lol.data.Product
import com.example.lol.data.fake.FakeProductRepository
import com.example.lol.domain.usecase.product.GetProductsUseCase
import com.example.lol.domain.usecase.product.SearchProductsUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogueViewModelTest {

    private lateinit var fakeRepository: FakeProductRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    private val localProducts =
            listOf(
                    Product(
                            id = 1,
                            title = "Шорты Вторник",
                            description = "Идеально подходят для летней погоды.",
                            price = 4000,
                            category = "Популярные",
                            imageUrl = "placeholder_shorts"
                    ),
                    Product(
                            id = 2,
                            title = "Рубашка Воскресенье",
                            description = "Отличный выбор для деловой встречи.",
                            price = 8000,
                            category = "Новинки",
                            imageUrl = "placeholder_shirt"
                    )
            )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeProductRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProducts uses local fallback when api returns empty list`() = runTest {
        fakeRepository.setProductsSuccess(emptyList())

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(localProducts, viewModel.products.value)
        assertEquals(localProducts, viewModel.allProducts.value)
        assertTrue(viewModel.catalogueState.value is CatalogueState.Success)
    }

    @Test
    fun `loadProducts keeps success state when api fails and local fallback exists`() = runTest {
        fakeRepository.setProductsError("API unavailable")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(localProducts, viewModel.products.value)
        assertTrue(viewModel.catalogueState.value is CatalogueState.Success)
    }

    @Test
    fun `filterProducts uses local fallback when api search returns empty`() = runTest {
        fakeRepository.setProductsSuccess(emptyList())
        fakeRepository.setSearchSuccess(emptyList())

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.filterProducts("летней")
        advanceUntilIdle()

        assertEquals(listOf(localProducts.first()), viewModel.products.value)
        assertEquals(1, fakeRepository.searchProductsCallCount)
    }

    @Test
    fun `filterProducts uses local fallback when api search fails`() = runTest {
        fakeRepository.setProductsSuccess(emptyList())
        fakeRepository.setSearchError("Search failed")

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.filterProducts("деловой")
        advanceUntilIdle()

        assertEquals(listOf(localProducts[1]), viewModel.products.value)
        assertEquals(1, fakeRepository.searchProductsCallCount)
        assertTrue(viewModel.catalogueState.value is CatalogueState.Success)
    }

    @Test
    fun `clearing search restores products even if previous api search finishes later`() = runTest {
        fakeRepository.setProductsSuccess(emptyList())
        fakeRepository.setSearchSuccess(emptyList())
        fakeRepository.setSearchDelay(1_000L)

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.filterProducts("неттакоготовара")
        viewModel.filterProducts("")
        advanceUntilIdle()

        assertEquals(localProducts, viewModel.products.value)
    }

    private fun createViewModel(): CatalogueViewModel {
        return CatalogueViewModel(
                application = mockk<Application>(relaxed = true),
                getProductsUseCase = GetProductsUseCase(fakeRepository),
                searchProductsUseCase = SearchProductsUseCase(fakeRepository),
                localProductsProvider = { localProducts },
                productsCacheSaver = {}
        )
    }
}
