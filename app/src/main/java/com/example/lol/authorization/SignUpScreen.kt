package com.example.lol.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import com.example.lol.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.InputBg
import com.example.lol.ui.theme.InputStroke
import com.example.lol.ui.theme.Title1Semibold
import com.example.lol.ui.theme.CaptionRegular
import com.example.lol.ui.theme.Title3Semibold
import com.example.lol.ui.theme.Roboto
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

import com.example.lol.components.AppTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
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
            AppTextField(value = name, onValueChange = { name = it }, placeholder = "Имя")
            // Surname
            AppTextField(value = surname, onValueChange = { surname = it }, placeholder = "Фамилия")
            // Patronymic
            AppTextField(value = patronymic, onValueChange = { patronymic = it }, placeholder = "Отчество")
            // Birthday
            AppTextField(value = birthday, onValueChange = { birthday = it }, placeholder = "Дата рождения")
            // Gender Dropdown Placeholder
            AppTextField(
                value = gender,
                onValueChange = { gender = it },
                placeholder = "Пол",
                trailingIcon = {
                     Icon(
                         painter = painterResource(id = R.drawable.icon_chevron_down),
                         contentDescription = "Dropdown",
                         tint = Color(0xFF7E7E9A)
                     )
                }
            )
            // Email (Почта)
            AppTextField(value = email, onValueChange = { email = it }, placeholder = "Почта")
        }
        
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                val emailPattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$".toRegex()
                if (!email.matches(emailPattern)) {
                    Toast.makeText(context, "Введите корректный email (например, name@domenname.ru)", Toast.LENGTH_SHORT).show()
                } else {
                    val sessionManager = SessionManager(context)
                    sessionManager.saveUserName(name)
                    sessionManager.saveEmail(email)
                    navController.navigate("EmailCode") 
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (name.isNotEmpty() && surname.isNotEmpty() && patronymic.isNotEmpty() && birthday.isNotEmpty() && email.isNotEmpty()) Color(0xFF1A6FEE) else Color(0xFFC9D4FB)
            ),
             shape = RoundedCornerShape(10.dp)
        ) {
            Text("Далее", color = Color.White, style = Title3Semibold)
        }
    }
}



