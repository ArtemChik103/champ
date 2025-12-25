package com.example.lol.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lol.R
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.Title1Semibold
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.Roboto
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailCodeScreen(navController: NavController, email: String = "example@mail.ru") {
    var code by remember { mutableStateOf("") }
    var timerSeconds by remember { mutableStateOf(60) }

    LaunchedEffect(Unit) {
        while (timerSeconds > 0) {
            delay(1000)
            timerSeconds--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Back Button
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .background(Color(0xFFF5F5F9), RoundedCornerShape(8.dp))
                    .size(32.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_chevron_left),
                    contentDescription = "Back",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(112.dp))

        // Title
        Text(
            text = "Введите код из E-mail",
            style = Title1Semibold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = "Мы отправили код на $email",
            style = TextRegular,
            color = Color(0xFF939396),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(34.dp))

        // Code Inputs (6 boxes)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            repeat(6) { index ->
                val char = if (index < code.length) code[index].toString() else ""
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFF5F5F9), RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = if (char.isNotEmpty()) AccentBlue else Color(0xFFEBEBEB),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        style = Title1Semibold,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Timer / Resend
        if (timerSeconds > 0) {
            Text(
                text = "Отправить снова через ${String.format("00:%02d", timerSeconds)}",
                style = TextRegular,
                color = Color(0xFF939396),
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "Отправить заново",
                style = TextRegular,
                color = AccentBlue,
                modifier = Modifier.clickable {
                    timerSeconds = 60
                },
                textAlign = TextAlign.Center
            )
        }
        
        // Custom Keyboard to match PinCodeScreen
        Spacer(modifier = Modifier.weight(1f))
        
        Column(
            modifier = Modifier.width(288.dp).padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9")
            ).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    row.forEach { digit ->
                        CodeKeyButton(text = digit) {
                            if (code.length < 6) {
                                code += digit
                                if (code.length == 6) {
                                    navController.navigate("CreatePassword")
                                }
                            }
                        }
                    }
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(80.dp))
                CodeKeyButton(text = "0") {
                    if (code.length < 6) {
                        code += "0"
                        if (code.length == 6) {
                            navController.navigate("CreatePassword")
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { if (code.isNotEmpty()) code = code.dropLast(1) },
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

@Composable
fun CodeKeyButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(Color(0xFFF5F5F9), RoundedCornerShape(40.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = Title1Semibold,
            color = Color.Black
        )
    }
}
