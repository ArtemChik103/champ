package com.example.lol.authorization

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import com.example.lol.R
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.InputBg
import com.example.lol.ui.theme.InputStroke
import com.example.lol.ui.theme.TextBlack
import com.example.lol.ui.theme.TextGray
import com.example.lol.ui.theme.Title1Semibold
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.Title3Semibold

import com.example.lol.components.AppTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePasswordScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                sessionManager.saveEmail("newuser@example.com") // Mock email
                Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                navController.navigate("CreatePin") {
                    popUpTo("CreatePassword") { inclusive = true }
                }
                viewModel.resetState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
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

        AppTextField(
            value = password,
            onValueChange = { password = it },
            label = "Пароль",
            placeholder = "••••••••",
            visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = R.drawable.eye_off_an_inner_journey_icon_svg_co),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
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
            onValueChange = { confirmPassword = it },
            label = "Повторите пароль",
            placeholder = "••••••••",
            visualTransformation = if (confirmPasswordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = painterResource(id = R.drawable.eye_off_an_inner_journey_icon_svg_co),
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                        tint = Color(0xFF7E7E9A),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { 
                val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?& ])[A-Za-z\\d@$!%*?& ]{8,}$".toRegex()
                if (password != confirmPassword) {
                    Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                } else if (!password.matches(passwordPattern)) {
                    Toast.makeText(context, "Пароль должен содержать минимум 8 символов, заглавные и строчные буквы, цифры и спецсимволы", Toast.LENGTH_LONG).show()
                } else {
                    val email = sessionManager.getEmail() ?: ""
                    sessionManager.savePassword(password)
                    viewModel.signUp(email, password) 
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (password.isNotEmpty() && confirmPassword.isNotEmpty()) AccentBlue else Color(0xFFC9D4FB),
                disabledContainerColor = Color(0xFFC9D4FB)
            ),
             shape = RoundedCornerShape(10.dp),
            enabled = authState !is AuthState.Loading
        ) {
             if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Далее", color = Color.White, style = Title3Semibold)
            }
        }
    }
}

