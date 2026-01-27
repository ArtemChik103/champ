package com.example.lol.authorization

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lol.R
import com.example.lol.components.AppTextField
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.CaptionRegular
import com.example.lol.ui.theme.TextBlack
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.Title1Semibold
import com.example.lol.ui.theme.Title3Semibold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePasswordScreen(navController: NavController) {
        val context = LocalContext.current
        val sessionManager = remember { SessionManager(context) }
        val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(sessionManager))
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        val authState by viewModel.authState.collectAsState()

        LaunchedEffect(authState) {
                when (authState) {
                        is AuthState.Success -> {
                                Toast.makeText(context, "Пароль установлен!", Toast.LENGTH_SHORT)
                                        .show()
                                navController.navigate("CreatePin") {
                                        popUpTo("CreatePassword") { inclusive = true }
                                }
                                viewModel.resetState()
                        }
                        is AuthState.Error -> {
                                Toast.makeText(
                                                context,
                                                (authState as AuthState.Error).message,
                                                Toast.LENGTH_SHORT
                                        )
                                        .show()
                                viewModel.resetState()
                        }
                        else -> {}
                }
        }
        // ... rest of the UI stays similar ...

        Column(
                modifier =
                        Modifier.fillMaxSize().background(Color.White).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Spacer(modifier = Modifier.height(103.dp))

                // Tittle Section
                Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Text(
                                text = "Создание пароля",
                                style = Title1Semibold,
                                color = TextBlack,
                                textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(23.dp))
                        Text(
                                text = "Создайте пароль для входа в приложение",
                                color = TextBlack,
                                style = TextRegular,
                                textAlign = TextAlign.Center
                        )
                }

                Spacer(modifier = Modifier.height(60.dp))

                // Password
                var passwordVisible by remember { mutableStateOf(false) }
                var confirmPasswordVisible by remember { mutableStateOf(false) }
                var isPasswordError by remember { mutableStateOf(false) }
                var isConfirmPasswordError by remember { mutableStateOf(false) }

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
                                if (isPasswordError) "Пароль не соответствует требованиям"
                                else null,
                        trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                                painter =
                                                        painterResource(
                                                                id =
                                                                        R.drawable
                                                                                .eye_off_an_inner_journey_icon_svg_co
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

                // Confirm Password
                AppTextField(
                        value = confirmPassword,
                        onValueChange = {
                                confirmPassword = it
                                isConfirmPasswordError = false
                        },
                        label = "Повторите пароль",
                        placeholder = "••••••••",
                        visualTransformation =
                                if (confirmPasswordVisible)
                                        androidx.compose.ui.text.input.VisualTransformation.None
                                else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        isError = isConfirmPasswordError,
                        errorMessage = if (isConfirmPasswordError) "Пароли не совпадают" else null,
                        trailingIcon = {
                                IconButton(
                                        onClick = {
                                                confirmPasswordVisible = !confirmPasswordVisible
                                        }
                                ) {
                                        Icon(
                                                painter =
                                                        painterResource(
                                                                id =
                                                                        R.drawable
                                                                                .eye_off_an_inner_journey_icon_svg_co
                                                        ),
                                                contentDescription =
                                                        if (confirmPasswordVisible) "Hide password"
                                                        else "Show password",
                                                tint = Color(0xFF7E7E9A),
                                                modifier = Modifier.size(24.dp)
                                        )
                                }
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Requirements
                val hasLength = password.length >= 8
                val hasCase = password.any { it.isLowerCase() } && password.any { it.isUpperCase() }
                val hasDigit = password.any { it.isDigit() }
                val hasSpecial = password.any { !it.isLetterOrDigit() && !it.isWhitespace() }

                Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                                "Требования к паролю:",
                                style = CaptionRegular,
                                color = Color(0xFF939396)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        RequirementItem("Минимум 8 символов", hasLength)
                        RequirementItem("Заглавные и строчные буквы", hasCase)
                        RequirementItem("Цифры", hasDigit)
                        RequirementItem("Спецсимволы", hasSpecial)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                        onClick = {
                                if (password != confirmPassword) {
                                        isConfirmPasswordError = true
                                        Toast.makeText(
                                                        context,
                                                        "Пароли не совпадают",
                                                        Toast.LENGTH_SHORT
                                                )
                                                .show()
                                } else if (!(hasLength && hasCase && hasDigit && hasSpecial)) {
                                        isPasswordError = true
                                        Toast.makeText(
                                                        context,
                                                        "Пароль не соответствует требованиям",
                                                        Toast.LENGTH_SHORT
                                                )
                                                .show()
                                } else {
                                        val email = sessionManager.getCurrentEmail() ?: ""
                                        sessionManager.savePassword(email, password)
                                        Toast.makeText(
                                                        context,
                                                        "Пароль установлен!",
                                                        Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        navController.navigate("CreatePin") {
                                                popUpTo("CreatePassword") { inclusive = true }
                                        }
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor =
                                                AccentBlue, // Always colored, validated on click
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
        }
}

@Composable
fun RequirementItem(text: String, isMet: Boolean) {
        Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                        modifier =
                                Modifier.size(6.dp)
                                        .background(
                                                if (isMet) Color(0xFF00BFA5) else Color(0xFF939396),
                                                androidx.compose.foundation.shape.CircleShape
                                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                        text = text,
                        style = CaptionRegular,
                        color = if (isMet) Color(0xFF00BFA5) else Color(0xFF939396)
                )
        }
}
