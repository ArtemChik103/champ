package com.example.lol.data

import android.content.Context
import com.example.lol.data.cache.ProductCache
import com.example.lol.data.repository.IProductRepository
import org.json.JSONArray
import java.io.IOException

class ProductRepository(private val context: Context) : IProductRepository {
    private val cache = ProductCache(context)

    override fun getProducts(): List<Product> {
        // Try to load from cache first if network/assets fails, or logic can be:
        // Load from assets (simulating network), save to cache, return. 
        // If assets fail, return cache.
        
        val cached = cache.getProducts()
        if (!cached.isNullOrEmpty()) {
             // ensure we update cache if needed, but for now return cached is fast
             // Real world: fetch network, if success -> save cache & return. if fail -> return cache.
        }

        val jsonString: String
        try {
            jsonString = context.assets.open("products.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return cached ?: emptyList()
        }

        val products = mutableListOf<Product>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val title = jsonObject.getString("title")
                val description = jsonObject.getString("description")
                val price = jsonObject.getInt("price")
                val category = jsonObject.getString("category")
                val imageUrl = jsonObject.getString("imageUrl")
                
                products.add(Product(id, title, description, price, category, imageUrl))
            }
            // Update cache
            cache.saveProducts(products)
        } catch (e: Exception) {
            e.printStackTrace()
            return cached ?: emptyList() 
        }
        return products
    }

    override fun getProductById(id: Int): Product? {
        // Optimized: call getProducts() which handles caching
        return getProducts().find { it.id == id }
    }
    
    override fun searchProducts(query: String): List<Product> {
        return getProducts().filter { 
            it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) 
        }
    }
}
