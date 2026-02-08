package com.example.lol.Profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lol.Cart.CartItem
import com.example.lol.data.network.NetworkResult
import com.example.lol.data.network.TokenManager
import com.example.lol.data.repository.IOrderRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Модель заказа.
 * @param id Уникальный идентификатор заказа
 * @param date Дата создания заказа
 * @param totalPrice Общая стоимость заказа (форматированная строка)
 * @param status Статус заказа
 * @param items Список товаров в заказе
 * @param serverIds Список ID записей на сервере (для каждого товара)
 */
data class Order(
        val id: String,
        val date: String,
        val totalPrice: String,
        val status: String,
        val items: List<CartItem>,
        val serverIds: List<String> = emptyList() // ID заказов на сервере
)

/** Состояние операций с заказами. */
sealed class OrderState {
    object Idle : OrderState()
    object Loading : OrderState()
    object Success : OrderState()
    data class Error(val message: String) : OrderState()
}

/**
 * ViewModel для управления заказами. Синхронизирует заказы с сервером через IOrderRepository и
 * кэширует локально.
 *
 * @param application Контекст приложения для SharedPreferences
 * @param orderRepository Репозиторий для работы с API заказов (опционально)
 * @param tokenManager Менеджер токенов для получения userId (опционально)
 */
class OrdersViewModel(
        application: Application,
        private val orderRepository: IOrderRepository? = null,
        private val tokenManager: TokenManager? = null
) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("orders_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    init {
        loadOrders()
    }

    /** Загрузка заказов из локального кэша. */
    private fun loadOrders() {
        val json = prefs.getString("orders_list", null)
        if (json != null) {
            val type = object : TypeToken<List<Order>>() {}.type
            _orders.value = gson.fromJson(json, type)
        }
    }

    /**
     * Создание заказа. При наличии API-репозитория синхронизирует каждый товар с сервером.
     * Сохраняет заказ локально для отображения в истории.
     *
     * @param items Список товаров корзины
     * @param totalPrice Общая стоимость заказа
     */
    fun addOrder(items: List<CartItem>, totalPrice: Double) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        // Форматирование цены
        val formattedPrice = String.format("%,.0f ₽", totalPrice).replace(",", " ")

        val orderId = "ORD-${UUID.randomUUID().toString().take(8).uppercase()}"

        val newOrder =
                Order(
                        id = orderId,
                        date = currentDate,
                        totalPrice = formattedPrice,
                        status = "В обработке",
                        items = items,
                        serverIds = emptyList()
                )

        // Добавляем заказ локально сразу (оптимистичное обновление)
        val updatedList =
                _orders.value.toMutableList().apply {
                    add(0, newOrder) // Добавляем в начало списка
                }
        _orders.value = updatedList
        saveOrders(updatedList)

        // Синхронизация с сервером
        syncOrderWithServer(orderId, items)
    }

    /** Синхронизация заказа с сервером. Создаёт запись для каждого товара в корзине. */
    private fun syncOrderWithServer(localOrderId: String, items: List<CartItem>) {
        val userId = tokenManager?.getUserId() ?: return
        val repo = orderRepository ?: return

        _orderState.value = OrderState.Loading

        viewModelScope.launch {
            val serverIds = mutableListOf<String>()
            var hasError = false
            var errorMessage = ""

            // Создаём заказ для каждого товара
            for (item in items) {
                when (val result =
                                repo.createOrder(
                                        userId = userId,
                                        productId = item.product.id.toString(),
                                        count = item.quantity
                                )
                ) {
                    is NetworkResult.Success -> {
                        serverIds.add(result.data.id)
                    }
                    is NetworkResult.Error -> {
                        hasError = true
                        errorMessage = result.message
                        // Продолжаем для остальных товаров
                    }
                    is NetworkResult.Loading -> {}
                }
            }

            // Обновляем заказ с серверными ID
            if (serverIds.isNotEmpty()) {
                _orders.value =
                        _orders.value.map { order ->
                            if (order.id == localOrderId) {
                                order.copy(
                                        serverIds = serverIds,
                                        status = if (hasError) "Частично оформлен" else "Оформлен"
                                )
                            } else {
                                order
                            }
                        }
                saveOrders(_orders.value)
            }

            _orderState.value =
                    if (hasError) {
                        OrderState.Error(errorMessage)
                    } else {
                        OrderState.Success
                    }
        }
    }

    /**
     * Удаление заказа из локального кэша.
     * @param orderId ID заказа для удаления
     */
    fun removeOrder(orderId: String) {
        val updatedList = _orders.value.filter { it.id != orderId }
        _orders.value = updatedList
        saveOrders(updatedList)
    }

    /** Сохранение заказов в локальный кэш. */
    private fun saveOrders(orders: List<Order>) {
        val json = gson.toJson(orders)
        prefs.edit().putString("orders_list", json).apply()
    }

    /** Сброс состояния. */
    fun resetState() {
        _orderState.value = OrderState.Idle
    }
}

/**
 * Factory для создания OrdersViewModel с зависимостями.
 *
 * @param application Контекст приложения
 * @param orderRepository Репозиторий заказов (опционально)
 * @param tokenManager Менеджер токенов (опционально)
 */
class OrdersViewModelFactory(
        private val application: Application,
        private val orderRepository: IOrderRepository? = null,
        private val tokenManager: TokenManager? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrdersViewModel(application, orderRepository, tokenManager) as T
    }
}
