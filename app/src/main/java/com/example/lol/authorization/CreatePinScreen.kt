package com.example.lol.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
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

@Composable
fun CreatePinScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    var pinCode by remember { mutableStateOf("") }
    val maxDigits = 4

    Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Spacer(modifier = Modifier.height(71.dp))

        // Title
        Text(
                text = "Создайте пин-код",
                style = Title1Semibold,
                color = TextBlack,
                textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
                text = "Для быстрого входа в приложение",
                style = TextRegular,
                color = Color(0xFF939396),
                textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(56.dp))

        // Dots (4)
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

        Spacer(modifier = Modifier.height(60.dp))

        // Keyboard
        Column(
                modifier = Modifier.width(288.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
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
                                            val email = sessionManager.getCurrentEmail() ?: ""
                                            sessionManager.savePin(email, pinCode)
                                            sessionManager.setLoggedIn(true)
                                            navController.navigate("Home")
                                        }
                                    }
                                }
                        )
                    }
                }
            }

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(80.dp))
                PinKeyButton(
                        text = "0",
                        onClick = {
                            if (pinCode.length < maxDigits) {
                                pinCode += "0"
                                if (pinCode.length == maxDigits) {
                                    val email = sessionManager.getCurrentEmail() ?: ""
                                    sessionManager.savePin(email, pinCode)
                                    sessionManager.setLoggedIn(true)
                                    navController.navigate("Home")
                                }
                            }
                        }
                )
                Box(
                        modifier =
                                Modifier.size(80.dp).clip(CircleShape).clickable(
                                                interactionSource =
                                                        remember { MutableInteractionSource() },
                                                indication =
                                                        ripple(bounded = true, color = AccentBlue)
                                        ) {
                                    if (pinCode.isNotEmpty()) pinCode = pinCode.dropLast(1)
                                },
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            painter = painterResource(id = R.drawable.icon_delete),
                            contentDescription = "Delete",
                            modifier = Modifier.size(35.dp, 24.dp)
                    )
                }
            }
        }
    }
}
