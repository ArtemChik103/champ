package com.example.lol.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.example.lol.data.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Определяет поведение и состояние компонента в рамках текущего модуля.
class ProductCache(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("product_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Сохраняет переданные данные в целевое хранилище.
     *
     * @param products Список товаров для сохранения или отображения.
     */
    fun saveProducts(products: List<Product>) {
        val json = gson.toJson(products)
        prefs.edit().putString("cached_products", json).apply()
    }

    // Возвращает актуальные данные из текущего источника состояния.
    fun getProducts(): List<Product>? {
        val json = prefs.getString("cached_products", null) ?: return null
        val type = object : TypeToken<List<Product>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }
    
    // Очищает связанные данные и приводит состояние к пустому виду.
    fun clearCache() {
        prefs.edit().clear().apply()
    }
}
