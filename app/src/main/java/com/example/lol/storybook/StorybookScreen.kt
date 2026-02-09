package com.example.lol.storybook

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lol.R
import com.example.lol.components.*
import com.example.lol.data.Product
import com.example.lol.ui.theme.*

// Отрисовывает экран и связывает пользовательские действия с состоянием UI.
@Composable
fun StorybookScreen() {
    LazyColumn(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { Text("Storybook", style = Title1ExtraBold) }

        // --- Colors ---
        // --- Цвета ---
        item {
            SectionTitle("Colors")
            ColorPalette()
        }

        // --- Typography ---
        // --- Типографика ---
        item {
            SectionTitle("Typography")
            Text("Title1ExtraBold", style = Title1ExtraBold)
            Text("Title1Semibold", style = Title1Semibold)
            Text("Title2Bold", style = Title2Bold)
            Text("Title3Semibold", style = Title3Semibold)
            Text("HeadlineMedium", style = HeadlineMedium)
            Text("TextMedium", style = TextMedium)
            Text("TextRegular", style = TextRegular)
            Text("CaptionRegular", style = CaptionRegular)
        }

        // --- Buttons ---
        // --- Кнопки ---
        item {
            SectionTitle("Buttons")
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Primary Button") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth(), enabled = false) {
                Text("Disabled Button")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("Outlined Button")
            }
        }

        // --- Text Fields ---
        // --- Текстовые поля ---
        item {
            SectionTitle("Text Fields")
            var text by remember { mutableStateOf("") }
            AppTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = "Label",
                    placeholder = "Placeholder"
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppTextField(
                    value = "Error Value",
                    onValueChange = {},
                    label = "Error State",
                    isError = true,
                    errorMessage = "This is an error"
            )
        }

        // --- Select Field ---
        // --- Поле выбора ---
        item {
            SectionTitle("Select Field")
            var selectedOption by remember { mutableStateOf("") }
            var selectedWithoutIcon by remember { mutableStateOf("") }

            AppSelectField(
                    value = selectedOption,
                    label = "Выберите пол",
                    placeholder = "Пол",
                    options = listOf("👨 Мужской", "👩 Женский"),
                    onOptionSelected = { selectedOption = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppSelectField(
                    value = selectedWithoutIcon,
                    label = "Select без иконки",
                    placeholder = "Выберите опцию",
                    options = listOf("🔥 Горячее", "✨ Новое", "✅ Подтверждено"),
                    onOptionSelected = { selectedWithoutIcon = it },
                    showChevron = false
            )
        }

        // --- Chips ---
        // --- Чипы ---
        item {
            SectionTitle("Chips ON/OFF")
            var chipState by remember { mutableStateOf(true) }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppChip(
                        text = "Активный",
                        isSelected = chipState,
                        onClick = { chipState = !chipState }
                )
                AppChip(
                        text = "Неактивный",
                        isSelected = !chipState,
                        onClick = { chipState = !chipState }
                )
            }
        }

        // --- Icons ---
        // --- Иконки ---
        item {
            SectionTitle("Icons")
            IconGrid()
        }

        // --- Search Bar ---
        // --- Поисковая строка ---
        item {
            SectionTitle("Search Bar")
            var searchQuery by remember { mutableStateOf("") }
            SearchBar(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Искать в каталоге"
            )
        }

        // --- App Header ---
        // --- Шапка приложения ---
        item {
            SectionTitle("App Header")
            Text("Title only:", style = CaptionRegular)
            AppHeader(title = "Каталог")

            Spacer(modifier = Modifier.height(8.dp))
            Text("Back button and Title:", style = CaptionRegular)
            AppHeader(title = "Профиль", showBackButton = true)

            Spacer(modifier = Modifier.height(8.dp))
            Text("With Trailing Icon (Profile):", style = CaptionRegular)
            AppHeader(
                    title = "Главная",
                    trailingIcon = {
                        Box(
                                modifier =
                                        Modifier.size(32.dp).background(Color.Black, CircleShape),
                                contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                    painter = painterResource(id = R.drawable.polzovatel_3),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                            )
                        }
                    }
            )
        }

        // --- Bottom Sheet ---
        // --- Нижний лист ---
        item {
            SectionTitle("Bottom Sheet")
            var showGenderSheet by remember { mutableStateOf(false) }
            var showPhotoSheet by remember { mutableStateOf(false) }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { showGenderSheet = true }, modifier = Modifier.weight(1f)) {
                    Text("Gender Sheet")
                }
                Button(onClick = { showPhotoSheet = true }, modifier = Modifier.weight(1f)) {
                    Text("Photo Sheet")
                }
            }

            if (showGenderSheet) {
                GenderSelectionSheet(
                        onDismiss = { showGenderSheet = false },
                        onGenderSelected = { showGenderSheet = false }
                )
            }

            if (showPhotoSheet) {
                PhotoPickerBottomSheet(
                        onDismiss = { showPhotoSheet = false },
                        onCameraClick = { showPhotoSheet = false },
                        onGalleryClick = { showPhotoSheet = false }
                )
            }
        }

        // --- Product Card ---
        // --- Карточка товара ---
        item {
            SectionTitle("Product Card")
            val sampleProduct =
                    Product(
                            id = 1,
                            title = "Nike Air Max 270",
                            description = "Great sneakers",
                            price = 12990,
                            category = "Sneakers",
                            imageUrl = "https://example.com/image.png"
                    )

            Text("Not in Cart:", style = CaptionRegular)
            Spacer(modifier = Modifier.height(4.dp))
            ProductCard(
                    product = sampleProduct,
                    onClick = {},
                    onAddToCart = {},
                    onRemoveFromCart = {},
                    isInCart = false
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("In Cart:", style = CaptionRegular)
            Spacer(modifier = Modifier.height(4.dp))
            ProductCard(
                    product = sampleProduct,
                    onClick = {},
                    onAddToCart = {},
                    onRemoveFromCart = {},
                    isInCart = true
            )
        }

        // --- Error Notification ---
        // --- Уведомление об ошибке ---
        item {
            SectionTitle("Error Notification")
            var errorMessage by remember { mutableStateOf<String?>(null) }

            Button(onClick = { errorMessage = "Произошла ошибка загрузки" }) {
                Text("Показать ошибку")
            }

            // Note: ErrorNotification uses absolute positioning/overlay,
            // so it might appear at the top of the screen, not inline.
            // Примечание: компонент ErrorNotification использует абсолютное позиционирование/оверлей,
            // поэтому уведомление может появляться в верхней части экрана, а не внутри строки.
            ErrorNotification(message = errorMessage, onDismiss = { errorMessage = null })
        }

        // --- TabBar ---
        // --- Таб-бар ---
        item {
            SectionTitle("TabBar (Preview)")
            StorybookBottomBar()
        }

        // Add some bottom padding
        // Добавляем небольшой отступ снизу.
        item { Spacer(modifier = Modifier.height(48.dp)) }
    }
}

/**
 * Отрисовывает composable-компонент в соответствии с переданным состоянием.
 *
 * @param title Заголовок, который отображается в интерфейсе.
 */
@Composable
fun SectionTitle(title: String) {
    Text(text = title, style = Title2Bold, modifier = Modifier.padding(bottom = 8.dp, top = 8.dp))
    HorizontalDivider(color = InputStroke)
}

// Отрисовывает composable-компонент в соответствии с переданным состоянием.
@Composable
fun ColorPalette() {
    val colors =
            listOf(
                    "AccentColor" to AccentColor,
                    "AccentBlue" to AccentBlue,
                    "AccentBlueInactive" to AccentBlueInactive,
                    "TextBlack" to TextBlack,
                    "TextGray" to TextGray,
                    "TextHint" to TextHint,
                    "InputBg" to InputBg,
                    "InputStroke" to InputStroke,
                    "RedError" to RedError,
                    "ChipInactiveBg" to ChipInactiveBg
            )

    Column {
        colors.chunked(2).forEach { rowColors ->
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowColors.forEach { (name, color) -> ColorItem(name, color, Modifier.weight(1f)) }
                if (rowColors.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * Отрисовывает элемент интерфейса и обрабатывает взаимодействие пользователя.
 *
 * @param name Имя пользователя или название сущности.
 * @param color Цветовое значение для стилизации визуального элемента интерфейса.
 * @param modifier Внешний `Modifier` для настройки размеров, отступов и поведения компонента.
 */
@Composable
fun ColorItem(name: String, color: Color, modifier: Modifier = Modifier) {
    Row(
            modifier =
                    modifier.background(Color.White, RoundedCornerShape(8.dp))
                            .border(1.dp, InputStroke, RoundedCornerShape(8.dp))
                            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
                modifier =
                        Modifier.size(40.dp)
                                .background(color, RoundedCornerShape(8.dp))
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, style = CaptionRegular, fontSize = 12.sp)
    }
}

// Отрисовывает composable-компонент в соответствии с переданным состоянием.
@Composable
fun IconGrid() {
    val icons =
            listOf(
                    R.drawable.icon_search,
                    R.drawable.icon_close,
                    R.drawable.icon_plus,
                    R.drawable.icon_minus,
                    R.drawable.icon_check,
                    R.drawable.icon_filter,
                    R.drawable.icon_shopping_cart,
                    R.drawable.icon_chevron_down,
                    R.drawable.icon_chevron_left
            )

    // Simple Grid workaround since LazyVerticalGrid cannot be nested inside LazyColumn easily
    // without fixed height
    // Using FlowRow-like layout with Columns

    // Упрощённый вариант сетки: компонент LazyVerticalGrid сложно корректно вложить в LazyColumn.
    // без фиксированной высоты.
    // Используем раскладку в стиле FlowRow через компоненты Column.
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        icons.take(5).forEach { iconId -> IconItem(iconId) }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        icons.drop(5).forEach { iconId -> IconItem(iconId) }
    }
}

/**
 * Отрисовывает элемент интерфейса и обрабатывает взаимодействие пользователя.
 *
 * @param iconId Идентификатор `icon` для выполнения операции.
 */
@Composable
fun IconItem(iconId: Int) {
    Box(
            modifier = Modifier.size(48.dp).background(InputBg, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
    ) {
        Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
        )
    }
}

// Отрисовывает панель интерфейса и синхронизирует активное состояние.
@Composable
fun StorybookBottomBar() {
    val items =
            listOf(
                    AppTabBarItem("Main", "Главная", R.drawable.analizy),
                    AppTabBarItem("Catalogue", "Каталог", R.drawable.rezultaty),
                    AppTabBarItem("Projects", "Проекты", R.drawable.podderzhka),
                    AppTabBarItem("Profile", "Профиль", R.drawable.polzovatel)
            )
    var selectedRoute by remember { mutableStateOf("Main") }

    AppTabBar(
            items = items,
            selectedRoute = selectedRoute,
            onItemSelected = { selectedRoute = it },
            modifier =
                    Modifier.height(88.dp)
                            .border(1.dp, Color(0xFFF4F4F4))
    )
}
