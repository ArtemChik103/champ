package com.example.lol.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lol.R
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.TextBlack
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.Title1Semibold

private val PinPressColor = Color(0xFF2074F2)

/**
 * Отрисовывает экран и связывает пользовательские действия с состоянием UI.
 *
 * @param navController Контроллер навигации для переходов между экранами и возврата по стеку.
 */
@Composable
fun PinCodeScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var pinCode by remember { mutableStateOf("") }
    val maxDigits = 4

    Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(144.dp))

        // Title
        // Заголовок
        Text(
                text = "Введите код",
                style = Title1Semibold,
                color = TextBlack,
                textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        // Описание
        Text(
                text = "Введите 4-значный код для входа",
                style = TextRegular,
                color = Color(0xFF939396),
                textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(56.dp))

        // Progress indicators (4 dots)
        // Индикаторы прогресса (4 точки)
        Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(maxDigits) { index ->
                Box(
                        modifier =
                                Modifier.size(16.dp)
                                        .then(
                                                if (index < pinCode.length) {
                                                    Modifier.background(AccentBlue, CircleShape)
                                                } else {
                                                    Modifier.border(1.dp, AccentBlue, CircleShape)
                                                }
                                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(76.dp))

        // Numeric keyboard
        // Цифровая клавиатура
        Column(
                modifier = Modifier.width(288.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Rows 1-3
            // Ряды 1-3
            listOf(listOf("1", "2", "3"), listOf("4", "5", "6"), listOf("7", "8", "9")).forEach {
                    row ->
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    row.forEach { digit ->
                        PinKeyButton(
                                text = digit,
                                onClick = {
                                    if (pinCode.length < maxDigits) {
                                        pinCode += digit
                                        if (pinCode.length == maxDigits) {
                                            val sessionManager = SessionManager(context)
                                            if (pinCode == sessionManager.getPin()) {
                                                sessionManager.setLoggedIn(true)
                                                sessionManager.resetOneShotInactivityNotificationCycle()
                                                navController.navigate("Home") {
                                                    popUpTo("PinCode") { inclusive = true }
                                                }
                                            } else {
                                                pinCode = ""
                                                android.widget.Toast.makeText(
                                                                context,
                                                                "Неверный код",
                                                                android.widget.Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                            }
                                        }
                                    }
                                }
                        )
                    }
                }
            }

            // Last row: empty, 0, delete
            // Последняя строка: пусто, 0, удалить
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                // Empty space - replaced with transparent box for spacing
                // Пустое место заменено прозрачным контейнером для сохранения отступов.
                Box(modifier = Modifier.size(80.dp))

                // 0
                PinKeyButton(
                        text = "0",
                        onClick = {
                            if (pinCode.length < maxDigits) {
                                pinCode += "0"
                                if (pinCode.length == maxDigits) {
                                    val sessionManager = SessionManager(context)
                                    if (pinCode == sessionManager.getPin()) {
                                        sessionManager.setLoggedIn(true)
                                        sessionManager.resetOneShotInactivityNotificationCycle()
                                        navController.navigate("Home") {
                                            popUpTo("PinCode") { inclusive = true }
                                        }
                                    } else {
                                        pinCode = ""
                                        android.widget.Toast.makeText(
                                                        context,
                                                        "Неверный код",
                                                        android.widget.Toast.LENGTH_SHORT
                                                )
                                                .show()
                                    }
                                }
                            }
                        }
                )

                // Delete button
                // Кнопка удаления
                Box(
                        modifier =
                                Modifier.size(80.dp)
                                        .clip(CircleShape)
                                        .clickable(
                                                interactionSource =
                                                        remember { MutableInteractionSource() },
                                                indication =
                                                        ripple(
                                                                bounded = true,
                                                                color = PinPressColor
                                                        ),
                                                onClick = {
                                                    if (pinCode.isNotEmpty()) {
                                                        pinCode = pinCode.dropLast(1)
                                                    }
                                                }
                                        ),
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            painter = painterResource(id = R.drawable.icon_delete),
                            contentDescription = "Delete",
                            tint = TextBlack,
                            modifier = Modifier.size(35.dp, 24.dp)
                    )
                }
            }
        }
    }
}

/**
 * Отрисовывает composable-компонент в соответствии с переданным состоянием.
 *
 * @param text Текстовое содержимое элемента интерфейса.
 * @param onClick Колбэк, вызываемый при нажатии пользователя.
 * @param isHighlighted Флаг визуальной подсветки кнопки или элемента.
 */
@Composable
fun PinKeyButton(text: String, onClick: () -> Unit, isHighlighted: Boolean = false) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
            modifier =
                    Modifier.size(80.dp)
                            .background(
                                    if (isHighlighted) AccentBlue else Color(0xFFF5F5F9),
                                    CircleShape
                            )
                            .clip(CircleShape)
                            .clickable(
                                    interactionSource = interactionSource,
                                    indication =
                                            ripple(bounded = true, color = PinPressColor),
                                    onClick = onClick
                            ),
            contentAlignment = Alignment.Center
    ) {
        Text(
                text = text,
                style = Title1Semibold,
                color = if (isHighlighted) Color.White else Color.Black
        )
    }
}
