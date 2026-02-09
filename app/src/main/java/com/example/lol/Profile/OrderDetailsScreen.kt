package com.example.lol.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lol.R
import com.example.lol.ui.theme.*

/**
 * Отрисовывает экран и связывает пользовательские действия с состоянием UI.
 *
 * @param navController Контроллер навигации для переходов между экранами и возврата по стеку.
 * @param viewModel ViewModel экрана с состоянием, событиями и бизнес-логикой.
 * @param orderId Идентификатор заказа для выборки или удаления.
 */
@Composable
fun OrderDetailsScreen(navController: NavController, viewModel: OrdersViewModel, orderId: String) {
        val orders by viewModel.orders.collectAsState()
        val order = orders.find { it.id == orderId }

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                // Header
                // Заголовок
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(
                                                top = 52.dp,
                                                bottom = 12.dp,
                                                start = 20.dp,
                                                end = 20.dp
                                        )
                ) {
                        IconButton(
                                onClick = {
                                        if (!navController.popBackStack()) {
                                                navController.navigate("MyOrders") {
                                                        popUpTo(
                                                                navController
                                                                        .graph
                                                                        .startDestinationId
                                                        ) { inclusive = true }
                                                }
                                        }
                                },
                                modifier =
                                        Modifier.align(Alignment.CenterStart)
                                                .size(32.dp)
                                                .background(
                                                        Color(0xFFF5F5F9),
                                                        RoundedCornerShape(8.dp)
                                                )
                        ) {
                                Icon(
                                        painter =
                                                painterResource(id = R.drawable.icon_chevron_left),
                                        contentDescription = "Back",
                                        tint = Color.Black,
                                        modifier = Modifier.size(24.dp)
                                )
                        }
                        Text(
                                text = "Детали заказа",
                                style = Title2Semibold,
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Black
                        )
                        if (order != null) {
                                IconButton(
                                        onClick = {
                                                viewModel.removeOrder(order.id)
                                                navController.popBackStack()
                                        },
                                        modifier = Modifier.align(Alignment.CenterEnd).size(32.dp)
                                ) {
                                        Icon(
                                                painter =
                                                        painterResource(
                                                                id = R.drawable.icon_delete
                                                        ),
                                                contentDescription = "Delete",
                                                tint = Color.Red,
                                                modifier = Modifier.size(24.dp)
                                        )
                                }
                        }
                }

                HorizontalDivider(color = Color(0xFFF4F4F4), thickness = 1.dp)

                if (order == null) {
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ) { Text("Заказ не найден", style = Title3Semibold, color = Color.Gray) }
                } else {
                        LazyColumn(
                                contentPadding = PaddingValues(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                // Order Info Card
                                // Карточка информации о заказе
                                item {
                                        Box(
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .background(
                                                                        Color(0xFFF5F5F9),
                                                                        RoundedCornerShape(12.dp)
                                                                )
                                                                .padding(16.dp)
                                        ) {
                                                Column {
                                                        Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                horizontalArrangement =
                                                                        Arrangement.SpaceBetween
                                                        ) {
                                                                Text(
                                                                        text = "Заказ ${order.id}",
                                                                        style = Title3Semibold,
                                                                        color = Color.Black
                                                                )
                                                                Text(
                                                                        text = order.status,
                                                                        style = CaptionSemibold,
                                                                        color =
                                                                                if (order.status ==
                                                                                                "Отменен"
                                                                                )
                                                                                        Color.Red
                                                                                else if (order.status ==
                                                                                                "В обработке"
                                                                                )
                                                                                        Color(
                                                                                                0xFFFFA500
                                                                                        )
                                                                                else
                                                                                        Color(
                                                                                                0xFF00BFA5
                                                                                        )
                                                                )
                                                        }
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                horizontalArrangement =
                                                                        Arrangement.SpaceBetween
                                                        ) {
                                                                Text(
                                                                        text = order.date,
                                                                        style = CaptionRegular,
                                                                        color = Color(0xFF939396)
                                                                )
                                                                Text(
                                                                        text = order.totalPrice,
                                                                        style = Title3Semibold,
                                                                        color = AccentBlue
                                                                )
                                                        }
                                                }
                                        }
                                }

                                // Items Header
                                // Заголовок списка товаров
                                item {
                                        Text(
                                                text = "Товары",
                                                style = Title3Semibold,
                                                color = Color.Black
                                        )
                                }

                                // Order Items
                                // Позиции заказа
                                items(order.items) { item ->
                                        Box(
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .background(
                                                                        Color.White,
                                                                        RoundedCornerShape(12.dp)
                                                                )
                                                                .padding(12.dp)
                                        ) {
                                                Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement =
                                                                Arrangement.SpaceBetween,
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        Column(modifier = Modifier.weight(1f)) {
                                                                Text(
                                                                        text = item.product.title,
                                                                        style = TextMedium,
                                                                        color = Color.Black
                                                                )
                                                                Spacer(
                                                                        modifier =
                                                                                Modifier.height(
                                                                                        4.dp
                                                                                )
                                                                )
                                                                Text(
                                                                        text =
                                                                                "${item.quantity} шт.",
                                                                        style = CaptionRegular,
                                                                        color = Color(0xFF939396)
                                                                )
                                                        }
                                                        Text(
                                                                text =
                                                                        "${item.product.price * item.quantity} ₽",
                                                                style = TextMedium,
                                                                color = Color.Black
                                                        )
                                                }
                                        }
                                        HorizontalDivider(
                                                color = Color(0xFFF4F4F4),
                                                thickness = 1.dp
                                        )
                                }
                        }
                }
        }
}
