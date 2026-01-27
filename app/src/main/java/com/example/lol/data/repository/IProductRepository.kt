package com.example.lol.data.repository

import com.example.lol.data.Product

interface IProductRepository {
    fun getProducts(): List<Product>
    fun getProductById(id: Int): Product?
    fun searchProducts(query: String): List<Product>
}
