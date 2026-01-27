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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lol.R
import com.example.lol.ui.theme.*

@Composable
fun MyOrdersScreen(navController: NavController) {
        val orders =
                listOf(
                        Order("ORD-2023-001", "12.01.2023", "3 500 ₽", "Доставлен"),
                        Order("ORD-2023-002", "15.02.2023", "12 000 ₽", "В обработке"),
                        Order("ORD-2023-003", "20.03.2023", "850 ₽", "Отменен")
                )

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                // Header
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
                                onClick = { navController.popBackStack() },
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
                                text = "Мои заказы",
                                style = Title2Semibold,
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Black
                        )
                }

                HorizontalDivider(color = Color(0xFFF4F4F4), thickness = 1.dp)

                if (orders.isEmpty()) {
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ) { Text("У вас нет заказов", style = Title3Semibold, color = Color.Gray) }
                } else {
                        LazyColumn(
                                contentPadding = PaddingValues(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) { items(orders) { order -> OrderCard(order) } }
                }
        }
}

data class Order(val id: String, val date: String, val price: String, val status: String)

@Composable
fun OrderCard(order: Order) {
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(Color(0xFFF5F5F9), RoundedCornerShape(12.dp))
                                .padding(16.dp)
        ) {
                Column {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                                Text(
                                        text = "Заказ №${order.id}",
                                        style = Title3Semibold,
                                        color = Color.Black
                                )
                                Text(text = order.price, style = Title3Semibold, color = AccentBlue)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                                Text(
                                        text = order.date,
                                        style = CaptionRegular,
                                        color = Color(0xFF939396)
                                )
                                Text(
                                        text = order.status,
                                        style = CaptionRegular,
                                        color =
                                                if (order.status == "Отменен") Color.Red
                                                else if (order.status == "В обработке")
                                                        Color(0xFFFFA500)
                                                else Color(0xFF00BFA5)
                                )
                        }
                }
        }
}
