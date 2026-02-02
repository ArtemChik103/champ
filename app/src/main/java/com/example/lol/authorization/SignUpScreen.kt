package com.example.lol.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lol.R
import com.example.lol.components.AppTextField
import com.example.lol.components.ErrorNotification
import com.example.lol.components.GenderSelectionSheet
import com.example.lol.data.network.RetrofitInstance
import com.example.lol.data.network.TokenManager
import com.example.lol.data.repository.AuthRepository
import com.example.lol.domain.usecase.auth.LoginUseCase
import com.example.lol.domain.usecase.auth.LogoutUseCase
import com.example.lol.domain.usecase.auth.RegisterUseCase
import com.example.lol.ui.theme.CaptionRegular
import com.example.lol.ui.theme.Title1Semibold
import com.example.lol.ui.theme.Title3Semibold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var showGenderSheet by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isNameError by remember { mutableStateOf(false) }
    var isSurnameError by remember { mutableStateOf(false) }
    var isPatronymicError by remember { mutableStateOf(false) }
    var isBirthdayError by remember { mutableStateOf(false) }
    var isGenderError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val tokenManager = remember { TokenManager(context) }
    val authRepository = remember { AuthRepository(RetrofitInstance.api, tokenManager) }
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
                navController.navigate("CreatePassword")
                viewModel.resetState()
            }
            is AuthState.Error -> {
                errorMessage = (authState as AuthState.Error).message
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
                modifier =
                        Modifier.fillMaxSize().background(Color.White).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Back Button
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                        modifier =
                                Modifier.size(32.dp)
                                        .background(Color(0xFFF5F5F9), RoundedCornerShape(8.dp))
                                        .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            painter = painterResource(id = R.drawable.icon_chevron_left),
                            contentDescription = "Back",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tittle Section
            Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                        text = "Создание Профиля",
                        style = Title1Semibold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp)) // Spacing adjustment

            // Hint messages
            Text(
                    text = "Без профиля вы не сможете создавать проекты.",
                    style = CaptionRegular,
                    color = Color(0xFF939396),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                    text = "В профиле будут храниться результаты проектов и ваши описания.",
                    style = CaptionRegular,
                    color = Color(0xFF939396),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Inputs Column
            Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp) // Gap from 6.css
            ) {
                // Name
                AppTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            isNameError = false
                        },
                        placeholder = "Имя",
                        isError = isNameError,
                        errorMessage = if (isNameError) "Введите имя" else null
                )
                // Surname
                AppTextField(
                        value = surname,
                        onValueChange = {
                            surname = it
                            isSurnameError = false
                        },
                        placeholder = "Фамилия",
                        isError = isSurnameError,
                        errorMessage = if (isSurnameError) "Введите фамилию" else null
                )
                // Patronymic
                AppTextField(
                        value = patronymic,
                        onValueChange = {
                            patronymic = it
                            isPatronymicError = false
                        },
                        placeholder = "Отчество",
                        isError = isPatronymicError,
                        errorMessage = if (isPatronymicError) "Введите отчество" else null
                )
                // Birthday DatePicker
                var showDatePicker by remember { mutableStateOf(false) }
                val datePickerState = androidx.compose.material3.rememberDatePickerState()

                Box(modifier = Modifier.clickable { showDatePicker = true }) {
                    AppTextField(
                            value = birthday,
                            onValueChange = {},
                            placeholder = "Дата рождения",
                            isError = isBirthdayError,
                            errorMessage = if (isBirthdayError) "Введите дату рождения" else null,
                            trailingIcon = {
                                Icon(
                                        painter = painterResource(id = R.drawable.icons2),
                                        contentDescription = "Calendar",
                                        tint = Color(0xFFBFC7D1),
                                        modifier = Modifier.size(20.dp)
                                )
                            }
                    )
                    Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
                }

                if (showDatePicker) {
                    androidx.compose.material3.DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                androidx.compose.material3.TextButton(
                                        onClick = {
                                            datePickerState.selectedDateMillis?.let {
                                                val date = java.util.Date(it)
                                                val format =
                                                        java.text.SimpleDateFormat(
                                                                "dd.MM.yyyy",
                                                                java.util.Locale.getDefault()
                                                        )
                                                birthday = format.format(date)
                                                isBirthdayError = false
                                            }
                                            showDatePicker = false
                                        }
                                ) { Text("OK") }
                            },
                            dismissButton = {
                                androidx.compose.material3.TextButton(
                                        onClick = { showDatePicker = false }
                                ) { Text("Отмена") }
                            }
                    ) { androidx.compose.material3.DatePicker(state = datePickerState) }
                }
                // Gender Dropdown
                Box(modifier = Modifier.clickable { showGenderSheet = true }) {
                    AppTextField(
                            value = gender,
                            onValueChange = {}, // Read only via sheet
                            placeholder = "Пол",
                            trailingIcon = {
                                Icon(
                                        painter =
                                                painterResource(id = R.drawable.icon_chevron_down),
                                        contentDescription = "Dropdown",
                                        tint = Color(0xFF7E7E9A)
                                )
                            },
                            isError = isGenderError,
                            errorMessage = if (isGenderError) "Выберите пол" else null
                    )
                    Box(modifier = Modifier.matchParentSize().clickable { showGenderSheet = true })
                }
                // Email (Почта)
                AppTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            isEmailError = false
                        },
                        placeholder = "Почта",
                        isError = isEmailError,
                        errorMessage =
                                if (isEmailError) "Некорректная почта (name@domain.ru)" else null
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                    onClick = {
                        // Validation
                        var isValid = true

                        if (name.isBlank()) {
                            isNameError = true
                            isValid = false
                        }
                        if (surname.isBlank()) {
                            isSurnameError = true
                            isValid = false
                        }
                        if (patronymic.isBlank()) {
                            isPatronymicError = true
                            isValid = false
                        }
                        if (birthday.isBlank()) {
                            isBirthdayError = true
                            isValid = false
                        }
                        if (gender.isBlank()) {
                            isGenderError = true
                            isValid = false
                        }

                        val emailPattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]+$".toRegex()
                        if (!email.matches(emailPattern)) {
                            isEmailError = true
                            isValid = false
                        }

                        if (!isValid) {
                            errorMessage = "Заполните все поля корректно"
                        } else {
                            viewModel.signUpLocal(email, name)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor =
                                            Color(
                                                    0xFF1A6FEE
                                            ) // Always enable, handle validation on click
                            ),
                    shape = RoundedCornerShape(10.dp)
            ) { Text("Далее", color = Color.White, style = Title3Semibold) }
        }

        if (showGenderSheet) {
            GenderSelectionSheet(
                    onDismiss = { showGenderSheet = false },
                    onGenderSelected = {
                        gender = it
                        isGenderError = false
                        showGenderSheet = false
                    }
            )
        }

        ErrorNotification(message = errorMessage, onDismiss = { errorMessage = null })
    } // End Box
}
