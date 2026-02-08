package com.example.lol.Projects

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lol.R
import com.example.lol.components.AppSelectField
import com.example.lol.components.AppTextField
import com.example.lol.components.PhotoPickerBottomSheet
import com.example.lol.ui.theme.*
import com.example.lol.ui.theme.Title2Semibold
import com.example.lol.ui.theme.Title3Semibold
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(navController: NavController, viewModel: ProjectsViewModel) {
    var projectType by remember { mutableStateOf("") }
    var projectName by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var recipient by remember { mutableStateOf("") }
    var descriptionSource by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var datePickingField by remember { mutableStateOf("") } // "start" or "end"

    val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
                    uri: Uri? ->
                selectedImageUri = uri
            }

    val isFormValid = projectName.isNotBlank()

    val cameraLauncher =
            rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.TakePicturePreview()
            ) { bitmap ->
                if (bitmap != null) {
                    // Камера возвращает Bitmap, поэтому храним его отдельно от URI из галереи.
                    selectedImageBitmap = bitmap
                    selectedImageUri = null
                }
            }

    var showPhotoSheet by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .padding(top = 52.dp, bottom = 12.dp, start = 20.dp, end = 20.dp)
        ) {
            IconButton(
                    onClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate("Projects") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                        painter = painterResource(id = R.drawable.icon_chevron_left),
                        contentDescription = "Назад",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                )
            }
            Text(
                    text = "Создать проект",
                    style = Title2Semibold,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
            )
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF4F4F4)))

        Column(
                modifier =
                        Modifier.weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            AppSelectField(
                    value = projectType,
                    options =
                            listOf(
                                    "Веб-сайт",
                                    "Мобильное приложение",
                                    "Маркетинг",
                                    "Дизайн-система"
                            ),
                    onOptionSelected = { projectType = it },
                    label = "Тип",
                    placeholder = "Выберите тип",
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                    value = projectName,
                    onValueChange = { projectName = it },
                    label = "Название проекта",
                    placeholder = "Введите имя",
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                    modifier =
                            Modifier.clickable {
                                datePickingField = "start"
                                showDatePicker = true
                            }
            ) {
                AppTextField(
                        value = startDate,
                        onValueChange = {},
                        label = "Дата начала",
                        placeholder = "--.--.----",
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                    painter = painterResource(id = R.drawable.icons2),
                                    contentDescription = "Calendar",
                                    tint = Color(0xFFBFC7D1),
                                    modifier = Modifier.size(20.dp)
                            )
                        }
                )
                Box(
                        modifier =
                                Modifier.matchParentSize().clickable {
                                    datePickingField = "start"
                                    showDatePicker = true
                                }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                    modifier =
                            Modifier.clickable {
                                datePickingField = "end"
                                showDatePicker = true
                            }
            ) {
                AppTextField(
                        value = endDate,
                        onValueChange = {},
                        label = "Дата Окончания",
                        placeholder = "--.--.----",
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                    painter = painterResource(id = R.drawable.icons2),
                                    contentDescription = "Calendar",
                                    tint = Color(0xFFBFC7D1),
                                    modifier = Modifier.size(20.dp)
                            )
                        }
                )
                Box(
                        modifier =
                                Modifier.matchParentSize().clickable {
                                    datePickingField = "end"
                                    showDatePicker = true
                                }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppSelectField(
                    value = recipient,
                    options =
                            listOf(
                                    "Команда разработчиков",
                                    "Отдел маркетинга",
                                    "Заказчик",
                                    "Личный проект"
                            ),
                    onOptionSelected = { recipient = it },
                    label = "Кому",
                    placeholder = "Выберите кому",
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                    value = descriptionSource,
                    onValueChange = { descriptionSource = it },
                    label = "Источник описания",
                    placeholder = "example.com",
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppSelectField(
                    value = category,
                    options = viewModel.getCategories(),
                    onOptionSelected = { category = it },
                    label = "Категория",
                    placeholder = "Выберите категорию",
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                    modifier =
                            Modifier.size(162.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .background(InputBg, RoundedCornerShape(16.dp))
                                    .clickable { showPhotoSheet = true },
                    contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null || selectedImageBitmap != null) {
                    val context = LocalContext.current
                    val bitmap =
                            if (selectedImageBitmap != null) {
                                selectedImageBitmap
                            } else {
                                remember(selectedImageUri) {
                                    try {
                                        if (Build.VERSION.SDK_INT < 28) {
                                            @Suppress("DEPRECATION")
                                            MediaStore.Images.Media.getBitmap(
                                                    context.contentResolver,
                                                    selectedImageUri
                                            )
                                        } else {
                                            val source =
                                                    ImageDecoder.createSource(
                                                            context.contentResolver,
                                                            selectedImageUri!!
                                                    )
                                            ImageDecoder.decodeBitmap(source)
                                        }
                                    } catch (e: Exception) {
                                        null
                                    }
                                }
                            }

                    if (bitmap != null) {
                        Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Selected image",
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                                painter = painterResource(id = R.drawable.icon_plus),
                                contentDescription = "Error loading image",
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                        )
                    }
                } else {
                    Icon(
                            painter = painterResource(id = R.drawable.icon_plus),
                            contentDescription = "Add photo",
                            tint = Color(0xFFBFC7D1),
                            modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                    onClick = {
                        if (projectName.isNotBlank()) {
                            val newProject =
                                    Project(
                                            name = projectName,
                                            type = projectType,
                                            startDate = startDate,
                                            endDate = endDate,
                                            recipient = recipient,
                                            descriptionSource = descriptionSource,
                                            category = category,
                                            imageUri = selectedImageUri?.toString()
                                    )
                            viewModel.addProject(newProject)
                            // Явная навигация на Projects с очисткой стека
                            navController.navigate("Projects") {
                                popUpTo("Projects") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                    shape = RoundedCornerShape(10.dp),
                    enabled = projectName.isNotBlank()
            ) { Text(text = "Подтвердить", style = Title3Semibold, color = Color.White) }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                            onClick = {
                                val date =
                                        datePickerState.selectedDateMillis?.let {
                                            val sdf =
                                                    SimpleDateFormat(
                                                            "dd.MM.yyyy",
                                                            Locale.getDefault()
                                                    )
                                            sdf.format(Date(it))
                                        }
                                                ?: ""
                                if (datePickingField == "start") startDate = date
                                else endDate = date
                                showDatePicker = false
                            }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
                }
        ) { DatePicker(state = datePickerState) }
    }

    if (showPhotoSheet) {
        PhotoPickerBottomSheet(
                onDismiss = { showPhotoSheet = false },
                onCameraClick = {
                    cameraLauncher.launch(null)
                    showPhotoSheet = false
                },
                onGalleryClick = {
                    launcher.launch("image/*")
                    showPhotoSheet = false
                }
        )
    }
}
