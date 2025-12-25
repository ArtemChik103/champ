package com.example.lol.data

import android.content.Context
import org.json.JSONArray
import java.io.IOException

class ProductRepository(private val context: Context) {

    fun getProducts(): List<Product> {
        val jsonString: String
        try {
            jsonString = context.assets.open("products.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return products
    }

    fun getProductById(id: Int): Product? {
        return getProducts().find { it.id == id }
    }
}
