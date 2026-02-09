package com.example.lol.data

import android.content.Context
import com.example.lol.data.cache.ProductCache
import com.example.lol.data.repository.IProductRepository
import org.json.JSONArray
import java.io.IOException

// Инкапсулирует работу с источниками данных и обработку результатов операций.
class ProductRepository(private val context: Context) : IProductRepository {
    private val cache = ProductCache(context)

    // Возвращает актуальные данные из текущего источника состояния.
    override fun getProducts(): List<Product> {
        val cached = cache.getProducts()

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
            cache.saveProducts(products)
        } catch (e: Exception) {
            e.printStackTrace()
            return cached ?: emptyList() 
        }
        return products
    }

    /**
     * Возвращает актуальные данные из текущего источника состояния.
     *
     * @param id Уникальный идентификатор сущности для целевой операции.
     */
    override fun getProductById(id: Int): Product? {
        return getProducts().find { it.id == id }
    }
    
    /**
     * Выполняет поиск по заданному запросу и возвращает отфильтрованный результат.
     *
     * @param query Поисковый запрос для фильтрации списка.
     */
    override fun searchProducts(query: String): List<Product> {
        return getProducts().filter { 
            it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) 
        }
    }
}
