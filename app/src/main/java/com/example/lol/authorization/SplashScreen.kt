package com.example.lol.authorization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.ImageLoader
import com.example.lol.R
import com.example.lol.ui.theme.SFProDisplay
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

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
    ) {
        // Load the SVG directly using Coil
        AsyncImage(
            model = R.raw.splash_bg,
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Text (Matule) rendered separately for better quality and alignment
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

