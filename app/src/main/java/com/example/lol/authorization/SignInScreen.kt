package com.example.lol.authorization

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lol.R
import com.example.lol.components.AppTextField
import com.example.lol.components.ErrorNotification
import com.example.lol.data.network.TokenManager
import com.example.lol.domain.usecase.auth.LoginUseCase
import com.example.lol.domain.usecase.auth.LogoutUseCase
import com.example.lol.domain.usecase.auth.RegisterUseCase
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.CaptionRegular
import com.example.lol.ui.theme.TextBlack
import com.example.lol.ui.theme.TextGray
import com.example.lol.ui.theme.TextMedium
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.Title1ExtraBold
import com.example.lol.ui.theme.Title3Medium
import com.example.lol.ui.theme.Title3Semibold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        val context = LocalContext.current
        val sessionManager = remember { SessionManager(context) }
        val tokenManager = remember { TokenManager(context) }
        val authRepository = remember { AuthRepositoryProvider.provide(tokenManager) }
        val loginUseCase = remember { LoginUseCase(authRepository) }
        val registerUseCase = remember { RegisterUseCase(authRepository) }
        val logoutUseCase = remember { LogoutUseCase(authRepository) }
        val viewModel: AuthViewModel =
                viewModel(
                        factory =
                                AuthViewModelFactory(
                                        loginUseCase,
                                        registerUseCase,
                                        logoutUseCase,
                                        tokenManager,
                                        sessionManager
                                )
                )
        val authState by viewModel.authState.collectAsState()

        LaunchedEffect(authState) {
                when (authState) {
                        is AuthState.Success -> {
                                sessionManager.setCurrentEmail(email)
                                sessionManager.setLoggedIn(true)
                                sessionManager.resetOneShotInactivityNotificationCycle()
                                Toast.makeText(context, "С возвращением!", Toast.LENGTH_SHORT)
                                        .show()
                                navController.navigate("Home") {
                                        popUpTo("SignIn") { inclusive = true }
                                }
                                viewModel.resetState()
                        }
                        is AuthState.Error -> {
                                errorMessage = (authState as AuthState.Error).message
                                viewModel.resetState()
                        }
                        else -> {}
                }
        }

        Column(
                modifier =
                        Modifier.fillMaxSize().background(Color.White).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Spacer(modifier = Modifier.height(103.dp))

                Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        AuthModeBanner()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                                text = "Добро пожаловать!",
                                style = Title1ExtraBold,
                                color = TextBlack,
                                textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(23.dp))
                        Text(
                                text = "Заполните данные для входа",
                                color = TextBlack,
                                style = TextRegular,
                                textAlign = TextAlign.Center
                        )
                }

                Spacer(modifier = Modifier.height(80.dp))

                var isEmailError by remember { mutableStateOf(false) }
                var isPasswordError by remember { mutableStateOf(false) }
                AppTextField(
                        value = email,
                        onValueChange = {
                                email = it
                                isEmailError = false
                        },
                        label = "Вход по E-mail",
                        placeholder = "example@mail.com",
                        modifier = Modifier.fillMaxWidth(),
                        isError = isEmailError,
                        errorMessage =
                                if (isEmailError) "Некорректная почта (name@domain.ru)" else null
                )

                Spacer(modifier = Modifier.height(16.dp))

                var passwordVisible by remember { mutableStateOf(false) }
                AppTextField(
                        value = password,
                        onValueChange = {
                                password = it
                                isPasswordError = false
                        },
                        label = "Пароль",
                        placeholder = "••••••••",
                        visualTransformation =
                                if (passwordVisible)
                                        androidx.compose.ui.text.input.VisualTransformation.None
                                else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        isError = isPasswordError,
                        errorMessage =
                                if (isPasswordError)
                                        "Пароль: мин. 8 символов, A-z, 0-9, спецсимволы"
                                else null,
                        trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                                painter =
                                                        painterResource(
                                                                id =
                                                                        if (passwordVisible)
                                                                                R.drawable
                                                                                        .eye_off_an_inner_journey_icon_svg_co
                                                                        else
                                                                                R.drawable
                                                                                        .eye_closed
                                                        ),
                                                contentDescription =
                                                        if (passwordVisible) "Hide password"
                                                        else "Show password",
                                                tint = Color(0xFF7E7E9A),
                                                modifier = Modifier.size(24.dp)
                                        )
                                }
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                                text = "Забыли пароль?",
                                color = TextGray,
                                style = CaptionRegular,
                                modifier =
                                        Modifier.align(Alignment.CenterEnd).clickable {
                                                /* TODO: открыть экран восстановления пароля */
                                        }
                        )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                        onClick = {
                                var isValid = true
                                if (!viewModel.isValidEmail(email)) {
                                        isEmailError = true
                                        isValid = false
                                }
                                if (!viewModel.isValidPassword(password)) {
                                        isPasswordError = true
                                        isValid = false
                                }

                                if (isValid) {
                                        viewModel.signIn(email, password)
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor =
                                                AccentBlue,
                                        disabledContainerColor = Color(0xFFC9D4FB)
                                ),
                        shape = RoundedCornerShape(10.dp),
                        enabled = authState !is AuthState.Loading
                ) {
                        if (authState is AuthState.Loading) {
                                CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(24.dp)
                                )
                        } else {
                                Text("Далее", color = Color.White, style = Title3Semibold)
                        }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Нет аккаунта? ", color = TextGray, style = TextRegular)
                        Text(
                                "Зарегистрироваться",
                                color = Color(0xFF2074F2),
                                style = TextMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { navController.navigate("SignUp") }
                        )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                        text = "Или войдите с помощью",
                        style = TextRegular,
                        color = Color(0xFF939396),
                        textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                        onClick = {
                                /* TODO: реализовать авторизацию через VK SDK */
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBEBEB)),
                        contentPadding = PaddingValues(0.dp)
                ) {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                        ) {
                                Icon(
                                        painter = painterResource(id = R.drawable.vkh4_logo),
                                        contentDescription = "VK Logo",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text("Войти с VK", color = Color.Black, style = Title3Medium)
                        }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                        onClick = {
                                /* TODO: реализовать авторизацию через Яндекс ID */
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBEBEB))
                ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                        painter = painterResource(id = R.drawable.vector),
                                        contentDescription = "Yandex Logo",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text("Войти с Яндекс", color = Color.Black, style = Title3Medium)
                        }
                }

                Spacer(modifier = Modifier.height(20.dp))
        }

        ErrorNotification(message = errorMessage, onDismiss = { errorMessage = null })
}
