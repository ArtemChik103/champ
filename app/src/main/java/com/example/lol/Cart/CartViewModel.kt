package com.example.lol.Cart

import androidx.lifecycle.ViewModel
import com.example.lol.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartItem(
    val product: Product,
    var quantity: Int
)

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(product: Product) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentItems + CartItem(product, 1)
            }
        }
    }

    fun removeFromCart(product: Product) {
        _cartItems.update { currentItems ->
            currentItems.filter { it.product.id != product.id }
        }
    }
    
    fun increaseQuantity(product: Product) {
        addToCart(product)
    }

    fun decreaseQuantity(product: Product) {
        _cartItems.update { currentItems ->
            currentItems.map { 
                if (it.product.id == product.id) {
                    it.copy(quantity = maxOf(0, it.quantity - 1))
                } else {
                    it
                }
            }.filter { it.quantity > 0 }
        }
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun calculateTotal(): Int {
        return _cartItems.value.sumOf { it.product.price * it.quantity }
    }
}
