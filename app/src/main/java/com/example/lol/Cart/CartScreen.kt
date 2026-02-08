package com.example.lol.Cart

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lol.R
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.HeadlineRegular
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.Title1Semibold
import com.example.lol.ui.theme.Title2Semibold
import com.example.lol.ui.theme.Title3Medium
import com.example.lol.ui.theme.Title3Semibold
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
        navController: NavController,
        viewModel: CartViewModel,
        ordersViewModel: com.example.lol.Profile.OrdersViewModel
) {
        val cartItems by viewModel.cartItems.collectAsState()
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var isProcessing by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                        Spacer(modifier = Modifier.height(52.dp))
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                IconButton(
                                        onClick = {
                                                if (!navController.popBackStack()) {
                                                        navController.navigate("Main") {
                                                                popUpTo(
                                                                        navController
                                                                                .graph
                                                                                .startDestinationId
                                                                ) { inclusive = true }
                                                        }
                                                }
                                        }
                                ) {
                                        Icon(
                                                painter =
                                                        painterResource(
                                                                id = R.drawable.icon_chevron_left
                                                        ),
                                                contentDescription = "Назад",
                                                tint = Color.Black,
                                                modifier = Modifier.size(24.dp)
                                        )
                                }
                                Text(text = "Корзина", style = Title1Semibold)
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        if (cartItems.isEmpty()) {
                                Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                ) { Text("Корзина пуста", color = Color.Gray) }
                        } else {
                                LazyColumn(
                                        modifier = Modifier.weight(1f),
                                        contentPadding = PaddingValues(bottom = 140.dp)
                                ) {
                                        items(cartItems) { item ->
                                                CartItemView(
                                                        item = item,
                                                        onRemove = {
                                                                viewModel.removeFromCart(
                                                                        item.product
                                                                )
                                                        },
                                                        onIncrease = {
                                                                viewModel.increaseQuantity(
                                                                        item.product
                                                                )
                                                        },
                                                        onDecrease = {
                                                                viewModel.decreaseQuantity(
                                                                        item.product
                                                                )
                                                        }
                                                )
                                                Spacer(modifier = Modifier.height(16.dp))
                                        }
                                }
                        }
                }

                if (cartItems.isNotEmpty()) {
                        Box(
                                modifier =
                                        Modifier.align(Alignment.BottomCenter)
                                                .fillMaxWidth()
                                                .background(Color.White)
                                                .shadow(
                                                        elevation = 16.dp,
                                                        shape =
                                                                RoundedCornerShape(
                                                                        topStart = 20.dp,
                                                                        topEnd = 20.dp
                                                                ),
                                                        spotColor = Color(0x99E4E8F5)
                                                )
                                                .padding(20.dp)
                        ) {
                                Column {
                                        Row(
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .padding(horizontal = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                                Text(
                                                        text = "Сумма",
                                                        style = Title2Semibold,
                                                        color = Color.Black
                                                )
                                                Text(
                                                        text = "${viewModel.calculateTotal()} ₽",
                                                        style = Title2Semibold,
                                                        color = Color.Black
                                                )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))

                                        Button(
                                                onClick = {
                                                        scope.launch {
                                                                isProcessing = true
                                                                ordersViewModel.addOrder(
                                                                        items = cartItems,
                                                                        totalPrice =
                                                                                viewModel
                                                                                        .calculateTotal()
                                                                                        .toDouble()
                                                                )

                                                                isProcessing = false
                                                                viewModel.clearCart()
                                                                Toast.makeText(
                                                                                context,
                                                                                "Заказ успешно оформлен!",
                                                                                Toast.LENGTH_LONG
                                                                        )
                                                                        .show()
                                                                navController.navigate("Main") {
                                                                        popUpTo("Main") {
                                                                                inclusive = false
                                                                        }
                                                                }
                                                        }
                                                },
                                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor = AccentBlue
                                                        ),
                                                shape =
                                                        RoundedCornerShape(10.dp),
                                                enabled = !isProcessing
                                        ) {
                                                Text(
                                                        text =
                                                                if (isProcessing) "Обработка..."
                                                                else "Оплатить",
                                                        style = Title3Semibold
                                                )
                                        }
                                }
                        }
                }
        }
}

@Composable
fun CartItemView(
        item: CartItem,
        onRemove: () -> Unit,
        onIncrease: () -> Unit,
        onDecrease: () -> Unit
) {
        Card(
                modifier =
                        Modifier.fillMaxWidth()
                                .height(138.dp)
                                .shadow(
                                        elevation = 20.dp,
                                        shape = RoundedCornerShape(12.dp),
                                        spotColor = Color(0x99E4E8F5)
                                ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF4F4F4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
                Box(modifier = Modifier.fillMaxSize()) {
                        Icon(
                                painter = painterResource(id = R.drawable.icon_delete),
                                contentDescription = "Delete",
                                tint = Color(0xFF7E7E9A),
                                modifier =
                                        Modifier.padding(top = 16.dp, end = 16.dp)
                                                .align(Alignment.TopEnd)
                                                .size(20.dp)
                                                .clickable(onClick = onRemove)
                        )

                        Text(
                                text = item.product.title,
                                style = HeadlineRegular,
                                color = Color.Black,
                                modifier =
                                        Modifier.padding(start = 16.dp, top = 16.dp).width(250.dp)
                        )

                        Text(
                                text = "${item.product.price * item.quantity} ₽",
                                style = Title3Medium,
                                color = Color.Black,
                                modifier =
                                        Modifier.align(Alignment.BottomStart)
                                                .padding(start = 16.dp, bottom = 20.dp)
                        )

                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier =
                                        Modifier.align(Alignment.BottomEnd)
                                                .padding(end = 16.dp, bottom = 16.dp)
                                                .width(64.dp)
                                                .height(32.dp)
                                                .background(
                                                        Color(0xFFF5F5F9),
                                                        RoundedCornerShape(8.dp)
                                                ),
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                                IconButton(onClick = onDecrease, modifier = Modifier.size(20.dp)) {
                                        Icon(
                                                painterResource(id = R.drawable.icon_minus),
                                                contentDescription = "Decrease",
                                                modifier = Modifier.size(12.dp),
                                                tint = Color(0xFFB8C1CC)
                                        )
                                }

                                Text(
                                        text = item.quantity.toString(),
                                        style = TextRegular,
                                        color = Color.Black
                                )

                                IconButton(onClick = onIncrease, modifier = Modifier.size(20.dp)) {
                                        Icon(
                                                painterResource(id = R.drawable.icon_plus),
                                                contentDescription = "Increase",
                                                modifier = Modifier.size(12.dp),
                                                tint = Color(0xFF939396)
                                        )
                                }
                        }
                }
        }
}
