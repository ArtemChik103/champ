package com.example.lol.Catalogue

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lol.data.Product
import com.example.lol.data.ProductRepository
import com.example.lol.data.cache.ProductCache
import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.models.ProductItem
import com.example.lol.domain.usecase.product.GetProductsUseCase
import com.example.lol.domain.usecase.product.SearchProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Состояние загрузки каталога. */
sealed class CatalogueState {
    object Idle : CatalogueState()
    object Loading : CatalogueState()
    object Success : CatalogueState()
    data class Error(val message: String) : CatalogueState()
}

/** ViewModel для управления каталогом продуктов. Использует UseCases для сетевых запросов. */
class CatalogueViewModel(
        application: Application,
        private val getProductsUseCase: GetProductsUseCase? = null,
        private val searchProductsUseCase: SearchProductsUseCase? = null
) : AndroidViewModel(application) {

    // Локальный репозиторий для fallback (JSON assets)
    private val localRepository = ProductRepository(application)

    // Кэш для offline-режима
    private val productCache = ProductCache(application)

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // Все продукты для баннеров (не затронуты фильтрами)
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> = _allProducts.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _catalogueState = MutableStateFlow<CatalogueState>(CatalogueState.Idle)
    val catalogueState: StateFlow<CatalogueState> = _catalogueState.asStateFlow()

    private var currentSearchQuery = ""
    private var apiProducts: List<ProductItem> = emptyList()

    init {
        loadProducts()
    }

    /** Загрузка продуктов: сначала API, затем fallback на локальные данные. */
    fun loadProducts() {
        viewModelScope.launch {
            _catalogueState.value = CatalogueState.Loading

            val useCase = getProductsUseCase
            if (useCase != null) {
                when (val result = useCase()) {
                    is NetworkResult.Success -> {
                        apiProducts = result.data
                        val products = result.data.map { it.toLocalProduct() }
                        _products.value = products
                        _allProducts.value = products
                        // Кэшируем для offline
                        cacheProducts(products)
                        _catalogueState.value = CatalogueState.Success
                    }
                    is NetworkResult.Error -> {
                        // Fallback на локальные данные
                        loadLocalProducts()
                        _catalogueState.value = CatalogueState.Error(result.message)
                    }
                    is NetworkResult.Loading -> {}
                }
            } else {
                // Нет API репозитория (UseCase) - используем локальные данные
                loadLocalProducts()
                _catalogueState.value = CatalogueState.Success
            }
        }
    }

    /** Загрузка продуктов из локального JSON. */
    private fun loadLocalProducts() {
        val productsList = localRepository.getProducts()
        _products.value = productsList
        _allProducts.value = productsList
    }

    /** Кэширование продуктов для offline-режима. */
    private fun cacheProducts(products: List<Product>) {
        productCache.saveProducts(products)
    }

    /** Установка категории для фильтрации. */
    fun setCategory(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    /** Поиск продуктов. При наличии API репозитория выполняет поиск через API. */
    fun filterProducts(query: String) {
        currentSearchQuery = query

        if (searchProductsUseCase != null && query.isNotBlank()) {
            searchViaApi(query)
        } else {
            applyFilters()
        }
    }

    /** Поиск через API. */
    private fun searchViaApi(query: String) {
        viewModelScope.launch {
            _catalogueState.value = CatalogueState.Loading

            val useCase = searchProductsUseCase ?: return@launch

            when (val result = useCase(query)) {
                is NetworkResult.Success -> {
                    val products = result.data.map { it.toLocalProduct() }
                    // Применяем фильтр категории к результатам поиска
                    _products.value =
                            if (_selectedCategory.value == "Все") {
                                products
                            } else {
                                products.filter {
                                    it.category.contains(_selectedCategory.value, ignoreCase = true)
                                }
                            }
                    _catalogueState.value = CatalogueState.Success
                }
                is NetworkResult.Error -> {
                    // Fallback на локальную фильтрацию
                    applyFilters()
                    _catalogueState.value = CatalogueState.Error(result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /** Применение фильтров (категория + поиск) локально. */
    private fun applyFilters() {
        val allProducts =
                if (apiProducts.isNotEmpty()) {
                    apiProducts.map { it.toLocalProduct() }
                } else {
                    localRepository.getProducts()
                }

        val categoryFiltered =
                if (_selectedCategory.value == "Все") {
                    allProducts
                } else {
                    allProducts.filter {
                        it.category.contains(_selectedCategory.value, ignoreCase = true)
                    }
                }

        if (currentSearchQuery.isBlank()) {
            _products.value = categoryFiltered
        } else {
            _products.value =
                    categoryFiltered.filter {
                        it.title.contains(currentSearchQuery, ignoreCase = true) ||
                                it.category.contains(currentSearchQuery, ignoreCase = true)
                    }
        }
    }

    /** Получение списка доступных категорий. */
    fun getCategories(): List<String> {
        val categories = _allProducts.value.map { it.category }.distinct().toMutableList()
        categories.add(0, "Все")
        return categories
    }

    /** Сброс состояния ошибки. */
    fun resetState() {
        _catalogueState.value = CatalogueState.Idle
    }

    /** Конвертация ProductItem из API в локальный Product. */
    private fun ProductItem.toLocalProduct(): Product {
        return Product(
                id = this.id.hashCode(), // Преобразуем String ID в Int
                title = this.title,
                description = "", // API не возвращает description в списке
                price = this.price,
                category = this.typeCloses.ifEmpty { this.type },
                imageUrl = "" // Изображения загружаются отдельно
        )
    }
}

/** Factory для создания CatalogueViewModel с зависимостями. */
class CatalogueViewModelFactory(
        private val application: Application,
        private val getProductsUseCase: GetProductsUseCase? = null,
        private val searchProductsUseCase: SearchProductsUseCase? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return CatalogueViewModel(application, null, null) as T
    }
}
