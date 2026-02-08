package com.example.lol.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.lol.R
import kotlinx.coroutines.delay

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
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}
