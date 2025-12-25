package com.example.lol.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

import com.example.lol.ui.theme.SFProDisplay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    LaunchedEffect(Unit) {
        delay(2500) // 2.5 seconds
        if (sessionManager.isLoggedIn()) {
            if (sessionManager.getPin() != null) {
                navController.navigate("PinCode") {
                    popUpTo("Splash") { inclusive = true }
                }
            } else {
                navController.navigate("CreatePin") {
                    popUpTo("Splash") { inclusive = true }
                }
            }
        } else {
            navController.navigate("SignIn") {
                popUpTo("Splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0.0f to Color(0xFF74C8E4),
                    0.5052f to Color(0x9973D5BC), // Mix of the middle gradient
                    1.0f to Color(0xFF74C8E4)
                )
            )
    ) {
        // Layering gradients and shapes for pixel-perfect match
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color(0x0D6269F0),
                        0.2917f to Color(0xA63740F5),
                        0.5f to Color(0xFF2254F5),
                        0.7135f to Color(0xA63740F5),
                        1.0f to Color(0x0D6269F0)
                    )
                )
        )

        // Ellipse 36 (Top-Left)
        Box(
            modifier = Modifier
                .size(453.dp)
                .offset(x = (-227).dp, y = (-227).dp)
                .blur(64.dp)
                .background(Color(0x9983A0F8), CircleShape)
        )

        // Ellipse 35 (Bottom-Right)
        Box(
            modifier = Modifier
                .size(453.dp)
                .offset(x = 148.dp, y = 585.dp)
                .blur(64.dp)
                .background(Color(0x9983A0F8), CircleShape)
        )
        
        Text(
            text = "Matule",
            color = Color.White,
            fontSize = 40.sp,
            fontFamily = SFProDisplay,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.03608.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

