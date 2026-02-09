package com.example.lol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lol.uikit.R
import com.example.lol.data.Product
import com.example.lol.ui.theme.AccentBlue

/**
 * Контент BottomSheet с подробной информацией о товаре.
 *
 * @param product Товар для отображения.
 * @param onAddToCart Обработчик добавления товара в корзину.
 * @param onClose Обработчик закрытия BottomSheet.
 */
/**
 * Отрисовывает composable-компонент в соответствии с переданным состоянием.
 *
 * @param product Модель товара, данные которой используются для отображения и действий.
 * @param onAddToCart Колбэк добавления товара в корзину.
 * @param onClose Колбэк закрытия текущего экрана или листа.
 */
@Composable
fun ProductDetailsContent(product: Product, onAddToCart: () -> Unit, onClose: () -> Unit) {
        Column(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                ) {
                        Text(
                                text = product.title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,
                                color = Color.Black,
                                lineHeight = 28.sp,
                                modifier = Modifier.weight(1f)
                        )

                        IconButton(
                                onClick = onClose,
                                modifier =
                                        Modifier.background(Color(0xFFF5F5F9), CircleShape)
                                                .size(24.dp)
                        ) {
                                Icon(
                                        painter = painterResource(id = R.drawable.icon_close),
                                        contentDescription = "Close",
                                        tint = Color(0xFF7E7E9A),
                                        modifier = Modifier.size(12.dp)
                                )
                        }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                        text = "Описание",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF939396)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                        text = product.description,
                        fontSize = 15.sp,
                        color = Color.Black,
                        lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Примерный расход:", fontSize = 14.sp, color = Color(0xFF939396))
                Text(
                        // Значение фиксировано в текущем макете.
                        text = "80-90 г",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                        onClick = onAddToCart,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                        shape = RoundedCornerShape(10.dp)
                ) {
                        Text(
                                text = "Добавить за ${product.price} ₽",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                        )
                }
        }
}
