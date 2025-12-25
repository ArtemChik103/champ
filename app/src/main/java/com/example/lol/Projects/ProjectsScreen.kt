package com.example.lol.Projects

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import com.example.lol.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.InputBg
import com.example.lol.ui.theme.Title2Semibold
import com.example.lol.ui.theme.Title3Semibold
import com.example.lol.ui.theme.CaptionRegular
import com.example.lol.ui.theme.CaptionSemibold
import com.example.lol.ui.theme.Roboto

@Composable
fun ProjectsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, bottom = 12.dp, start = 20.dp, end = 20.dp)
        ) {
            Text(
                text = "Проекты",
                style = Title2Semibold,
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_plus),
                contentDescription = "Создать проект",
                tint = Color(0xFFB8C1CC),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickable { navController.navigate("CreateProject") }
            )
        }
        
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF4F4F4)))
        
        // Projects List
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(4) {
                ProjectCard(
                    title = if (it == 0) "Мой первый проект" else "Название проекта",
                    date = if (it == 0) "Прошло 2 дня" else "Вчера",
                    onClick = { /* Navigate to Details */ }
                )
            }
        }
        
        // Add Project Button is fixed in the list or can be removed if header plus is enough
        // According to screen6.png, there's no big blue button at the bottom of the list, 
        // but 1.css implies a tabbar. Assuming the list is the main content.
    }
}

@Composable
fun ProjectCard(
    title: String,
    date: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Soft shadow
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = Title3Semibold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = date,
                    style = CaptionRegular,
                    color = Color(0xFF939396) // Caption gray
                )
            }
            
            Button(
                onClick = onClick,
                modifier = Modifier
                    .width(114.dp)
                    .height(35.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "Открыть",
                    style = CaptionSemibold,
                    color = Color.White
                )
            }
        }
    }
}
