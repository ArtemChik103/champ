package com.example.lol.Catalogue

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.lol.data.Product
import com.example.lol.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CatalogueViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProductRepository(application)
    
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    
    // All products for banners (not affected by filters)
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> = _allProducts.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private var currentSearchQuery = ""

    init {
        loadProducts()
    }

    private fun loadProducts() {
        val productsList = repository.getProducts()
        _products.value = productsList
        _allProducts.value = productsList
    }
    
    fun setCategory(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    fun filterProducts(query: String) {
        currentSearchQuery = query
        applyFilters()
    }

    private fun applyFilters() {
        val allProducts = repository.getProducts()
        val categoryFiltered = if (_selectedCategory.value == "Все") {
            allProducts
        } else {
            allProducts.filter { it.category.contains(_selectedCategory.value, ignoreCase = true) }
        }

        if (currentSearchQuery.isBlank()) {
            _products.value = categoryFiltered
        } else {
            _products.value = categoryFiltered.filter { 
                it.title.contains(currentSearchQuery, ignoreCase = true) || it.category.contains(currentSearchQuery, ignoreCase = true) 
            }
        }
    }
}
