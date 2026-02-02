package com.example.lol.Cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lol.data.Product
import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.TokenManager
import com.example.lol.domain.usecase.cart.AddToCartUseCase
import com.example.lol.domain.usecase.cart.UpdateCartItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Элемент корзины с продуктом и количеством. */
data class CartItem(
        val product: Product,
        var quantity: Int,
        val cartItemId: String? = null // ID записи в API для обновления
)

/** Состояние корзины. */
sealed class CartState {
    object Idle : CartState()
    object Loading : CartState()
    object Success : CartState()
    data class Error(val message: String) : CartState()
}

/** ViewModel для управления корзиной. Синхронизирует локальное состояние с API через UseCases. */
class CartViewModel(
        private val addToCartUseCase: AddToCartUseCase? = null,
        private val updateCartItemUseCase: UpdateCartItemUseCase? = null,
        private val tokenManager: TokenManager? = null
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _cartState = MutableStateFlow<CartState>(CartState.Idle)
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    /** Добавление продукта в корзину. Если репозиторий доступен, синхронизирует с сервером. */
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

        // Синхронизация с API если доступен
        syncAddToCart(product)
    }

    /** Синхронизация добавления товара с сервером. */
    private fun syncAddToCart(product: Product) {
        val userId = tokenManager?.getUserId() ?: return
        val useCase = addToCartUseCase ?: return

        viewModelScope.launch {
            when (val result = useCase(userId, product.id.toString(), 1)) {
                is NetworkResult.Success -> {
                    // Обновляем cartItemId для возможности последующего обновления
                    _cartItems.update { items ->
                        items.map {
                            if (it.product.id == product.id && it.cartItemId == null) {
                                it.copy(cartItemId = result.data.id)
                            } else {
                                it
                            }
                        }
                    }
                }
                is NetworkResult.Error -> {
                    // Логируем ошибку, но не откатываем локальное изменение
                    _cartState.value = CartState.Error(result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /** Удаление продукта из корзины. */
    fun removeFromCart(product: Product) {
        val item = _cartItems.value.find { it.product.id == product.id }

        _cartItems.update { currentItems -> currentItems.filter { it.product.id != product.id } }

        // Синхронизация с API (удаление = count = 0)
        item?.cartItemId?.let { cartItemId -> syncUpdateCartItem(cartItemId, product, 0) }
    }

    /** Увеличение количества товара. */
    fun increaseQuantity(product: Product) {
        val item = _cartItems.value.find { it.product.id == product.id }
        val newQuantity = (item?.quantity ?: 0) + 1

        _cartItems.update { currentItems ->
            currentItems.map {
                if (it.product.id == product.id) it.copy(quantity = newQuantity) else it
            }
        }

        item?.cartItemId?.let { cartItemId -> syncUpdateCartItem(cartItemId, product, newQuantity) }
    }

    /** Уменьшение количества товара. */
    fun decreaseQuantity(product: Product) {
        val item = _cartItems.value.find { it.product.id == product.id }
        val newQuantity = maxOf(0, (item?.quantity ?: 0) - 1)

        _cartItems.update { currentItems ->
            currentItems
                    .map {
                        if (it.product.id == product.id) {
                            it.copy(quantity = newQuantity)
                        } else {
                            it
                        }
                    }
                    .filter { it.quantity > 0 }
        }

        item?.cartItemId?.let { cartItemId -> syncUpdateCartItem(cartItemId, product, newQuantity) }
    }

    /** Синхронизация обновления количества товара с сервером. */
    private fun syncUpdateCartItem(cartItemId: String, product: Product, count: Int) {
        val userId = tokenManager?.getUserId() ?: return
        val useCase = updateCartItemUseCase ?: return

        viewModelScope.launch {
            when (val result = useCase(cartItemId, userId, product.id.toString(), count)) {
                is NetworkResult.Success -> {
                    _cartState.value = CartState.Success
                }
                is NetworkResult.Error -> {
                    _cartState.value = CartState.Error(result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /** Очистка корзины. */
    fun clearCart() {
        _cartItems.value = emptyList()
        _cartState.value = CartState.Success
    }

    /** Расчёт общей стоимости корзины. */
    fun calculateTotal(): Int {
        return _cartItems.value.sumOf { it.product.price * it.quantity }
    }

    /** Проверка наличия товара в корзине. */
    fun isInCart(productId: Int): Boolean {
        return _cartItems.value.any { it.product.id == productId }
    }

    /** Получение количества товара в корзине. */
    fun getQuantity(productId: Int): Int {
        return _cartItems.value.find { it.product.id == productId }?.quantity ?: 0
    }

    /** Сброс состояния ошибки. */
    fun resetState() {
        _cartState.value = CartState.Idle
    }
}

/** Factory для создания CartViewModel с зависимостями. */
class CartViewModelFactory(
        private val addToCartUseCase: AddToCartUseCase? = null,
        private val updateCartItemUseCase: UpdateCartItemUseCase? = null,
        private val tokenManager: TokenManager? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(addToCartUseCase, updateCartItemUseCase, tokenManager) as T
    }
}
