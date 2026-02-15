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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/** Состояние загрузки каталога. */
// Определяет поведение и состояние компонента в рамках текущего модуля.
sealed class CatalogueState {
    object Idle : CatalogueState()
    object Loading : CatalogueState()
    object Success : CatalogueState()
    data class Error(val message: String) : CatalogueState()
}

/** ViewModel для управления каталогом продуктов. Использует UseCases для сетевых запросов. */
// Хранит состояние экрана и координирует действия пользователя.
class CatalogueViewModel(
        application: Application,
        private val getProductsUseCase: GetProductsUseCase? = null,
        private val searchProductsUseCase: SearchProductsUseCase? = null,
        private val localProductsProvider: (() -> List<Product>)? = null,
        private val productsCacheSaver: ((List<Product>) -> Unit)? = null
) : AndroidViewModel(application) {

    private val localRepository by lazy { ProductRepository(application) }
    private val productCache by lazy { ProductCache(application) }

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
    private var searchJob: Job? = null

    init {
        loadProducts()
    }

    /** Загрузка продуктов: сначала API, затем fallback на локальные данные. */
    // Загружает данные, обрабатывает результат и обновляет состояние.
    fun loadProducts() {
        viewModelScope.launch {
            _catalogueState.value = CatalogueState.Loading

            val useCase = getProductsUseCase
            if (useCase != null) {
                when (val result = useCase()) {
                    is NetworkResult.Success -> {
                        val products = result.data.map { it.toLocalProduct() }
                        if (products.isNotEmpty()) {
                            _allProducts.value = products
                            applyFilters(products)
                            cacheProducts(products)
                            _catalogueState.value = CatalogueState.Success
                        } else {
                            val localProducts = loadLocalProducts()
                            _catalogueState.value =
                                    if (localProducts.isNotEmpty()) {
                                        CatalogueState.Success
                                    } else {
                                        CatalogueState.Error("Не удалось загрузить товары")
                                    }
                        }
                    }
                    is NetworkResult.Error -> {
                        val localProducts = loadLocalProducts()
                        _catalogueState.value =
                                if (localProducts.isNotEmpty()) {
                                    CatalogueState.Success
                                } else {
                                    CatalogueState.Error(result.message)
                                }
                    }
                    is NetworkResult.Loading -> {}
                }
            } else {
                val localProducts = loadLocalProducts()
                _catalogueState.value =
                        if (localProducts.isNotEmpty()) {
                            CatalogueState.Success
                        } else {
                            CatalogueState.Error("Не удалось загрузить товары")
                        }
            }
        }
    }

    /** Загрузка продуктов из локального JSON. */
    private fun loadLocalProducts(): List<Product> {
        val productsList = localProductsProvider?.invoke() ?: localRepository.getProducts()
        _allProducts.value = productsList
        applyFilters(productsList)
        return productsList
    }

    /** Кэширование продуктов для offline-режима. */
    /**
     * Сохраняет загруженный список товаров в локальный кэш для повторного использования.
     *
     * @param products Список товаров для сохранения или отображения.
     */
    private fun cacheProducts(products: List<Product>) {
        productsCacheSaver?.invoke(products) ?: productCache.saveProducts(products)
    }

    /** Установка категории для фильтрации. */
    /**
     * Обновляет значение в локальном состоянии или постоянном хранилище.
     *
     * @param category Категория, по которой выполняется фильтрация или сохранение.
     */
    fun setCategory(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    /** Поиск продуктов. При наличии API репозитория выполняет поиск через API. */
    /**
     * Применяет фильтрацию к данным и обновляет отображаемый результат.
     *
     * @param query Поисковый запрос для фильтрации списка.
     */
    fun filterProducts(query: String) {
        currentSearchQuery = query

        if (searchProductsUseCase != null && query.isNotBlank()) {
            searchViaApi(query)
        } else {
            searchJob?.cancel()
            searchJob = null
            applyFilters()
            _catalogueState.value = CatalogueState.Success
        }
    }

    /** Поиск через API. */
    /**
     * Выполняет поиск по заданному запросу и возвращает отфильтрованный результат.
     *
     * @param query Поисковый запрос для фильтрации списка.
     */
    private fun searchViaApi(query: String) {
        searchJob?.cancel()
        searchJob =
                viewModelScope.launch {
            _catalogueState.value = CatalogueState.Loading

            val useCase = searchProductsUseCase ?: return@launch

            when (val result = useCase(query)) {
                is NetworkResult.Success -> {
                    if (query != currentSearchQuery) return@launch
                    val products = result.data.map { it.toLocalProduct() }
                    val sourceProducts = if (products.isNotEmpty()) products else _allProducts.value
                    _products.value =
                            filterInMemory(
                                    sourceProducts = sourceProducts,
                                    category = _selectedCategory.value,
                                    query = query
                            )
                    _catalogueState.value = CatalogueState.Success
                }
                is NetworkResult.Error -> {
                    if (query != currentSearchQuery) return@launch
                    applyFilters()
                    _catalogueState.value =
                            if (_products.value.isNotEmpty()) {
                                CatalogueState.Success
                            } else {
                                CatalogueState.Error(result.message)
                            }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /** Применение фильтров (категория + поиск) локально. */
    private fun applyFilters(sourceProducts: List<Product> = _allProducts.value) {
        _products.value =
                filterInMemory(
                        sourceProducts = sourceProducts,
                        category = _selectedCategory.value,
                        query = currentSearchQuery
                )
    }

    private fun filterInMemory(
            sourceProducts: List<Product>,
            category: String,
            query: String
    ): List<Product> {
        val categoryFiltered =
                if (category == "Все") {
                    sourceProducts
                } else {
                    sourceProducts.filter { it.category.contains(category, ignoreCase = true) }
                }

        if (query.isBlank()) {
            return categoryFiltered
        }

        return categoryFiltered.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }

    /** Получение списка доступных категорий. */
    // Возвращает актуальные данные из текущего источника состояния.
    fun getCategories(): List<String> {
        val categories =
                _allProducts.value
                        .map { it.category }
                        .filter { it.isNotBlank() && !it.equals("Все", ignoreCase = true) }
                        .distinct()

        return listOf("Все") + categories
    }

    /** Сброс состояния ошибки. */
    // Сбрасывает состояние к исходным значениям по умолчанию.
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
// Создает экземпляры компонентов с необходимыми зависимостями.
class CatalogueViewModelFactory(
        private val application: Application,
        private val getProductsUseCase: GetProductsUseCase? = null,
        private val searchProductsUseCase: SearchProductsUseCase? = null
) : ViewModelProvider.Factory {
    /**
     * Создает новую сущность на основе переданных данных.
     *
     * @param modelClass Класс ViewModel, для которого создается экземпляр.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return CatalogueViewModel(application, getProductsUseCase, searchProductsUseCase) as T
    }
}
