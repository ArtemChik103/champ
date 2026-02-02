package com.example.lol.Projects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lol.R // Ensure this R is correct
import com.example.lol.ui.theme.*

@Composable
fun ProjectDetailsScreen(
        navController: NavController,
        viewModel: ProjectsViewModel,
        projectId: String
) {
        val projects by viewModel.projects.collectAsState()
        val project = projects.find { it.id == projectId }

        if (project == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Проект не найден", style = Title3Semibold, color = Color.Gray)
                }
                return
        }

        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .background(Color.White)
                                .verticalScroll(rememberScrollState())
        ) {
                // Header
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(
                                                top = 52.dp,
                                                bottom = 12.dp,
                                                start = 20.dp,
                                                end = 20.dp
                                        )
                ) {
                        IconButton(
                                onClick = { navController.popBackStack() },
                                modifier =
                                        Modifier.align(Alignment.CenterStart)
                                                .size(32.dp)
                                                .background(
                                                        Color(0xFFF5F5F9),
                                                        RoundedCornerShape(8.dp)
                                                )
                        ) {
                                Icon(
                                        painter =
                                                painterResource(id = R.drawable.icon_chevron_left),
                                        contentDescription = "Back",
                                        tint = Color.Black,
                                        modifier = Modifier.size(24.dp)
                                )
                        }
                        Text(
                                text = project.name,
                                style = Title2Semibold,
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Black
                        )
                        IconButton(
                                onClick = {
                                        viewModel.removeProject(project.id)
                                        navController.popBackStack()
                                },
                                modifier = Modifier.align(Alignment.CenterEnd).size(32.dp)
                        ) {
                                Icon(
                                        painter = painterResource(id = R.drawable.icon_delete),
                                        contentDescription = "Delete",
                                        tint = Color.Red,
                                        modifier = Modifier.size(24.dp)
                                )
                        }
                }

                HorizontalDivider(color = Color(0xFFF5F5F9), thickness = 1.dp)

                Column(modifier = Modifier.padding(20.dp)) {
                        // Image
                        if (project.imageUri != null) {
                                AsyncImage(
                                        model = project.imageUri,
                                        contentDescription = "Project Image",
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .height(200.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(Color(0xFFF5F5F9)),
                                        contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                        } else {
                                Box(
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .height(200.dp) // Placeholder height
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(Color(0xFFF5F5F9)),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Icon(
                                                painter =
                                                        painterResource(
                                                                id = R.drawable.icon_file_text
                                                        ), // Using existing icon as placeholder
                                                contentDescription = null,
                                                tint = Color(0xFFBFC7D1),
                                                modifier = Modifier.size(48.dp)
                                        )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Info Section
                        Text(text = "Информация", style = Title3Semibold, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))

                        InfoRow(
                                label = "Категория",
                                value = project.category.ifEmpty { "Не указана" }
                        )
                        InfoRow(
                                label = "Дата начала",
                                value = project.startDate.ifEmpty { "Не указана" }
                        )
                        InfoRow(
                                label = "Дата окончания",
                                value = project.endDate.ifEmpty { "Не указана" }
                        )
                        InfoRow(
                                label = "Получатель",
                                value = project.recipient.ifEmpty { "Не указан" }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Description
                        Text(text = "Описание", style = Title3Semibold, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                                text = project.descriptionSource.ifEmpty { "Нет описания" },
                                style = TextRegular,
                                color = Color(0xFF939396)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Results (Placeholder)
                        Text(text = "Результаты", style = Title3Semibold, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .height(100.dp)
                                                .background(
                                                        Color(0xFFF5F5F9),
                                                        RoundedCornerShape(12.dp)
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                                Text(
                                        text = "Здесь будут результаты проекта",
                                        style = CaptionRegular,
                                        color = Color(0xFF939396)
                                )
                        }
                }
        }
}

@Composable
fun InfoRow(label: String, value: String) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
                Text(text = label, style = TextRegular, color = Color(0xFF939396))
                Text(text = value, style = TextMedium, color = Color.Black)
        }
}
