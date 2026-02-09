package com.example.lol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.Title3Semibold

/**
 * BottomSheet выбора источника фотографии.
 *
 * @param onDismiss Колбэк закрытия BottomSheet.
 * @param onCameraClick Обработчик выбора камеры.
 * @param onGalleryClick Обработчик выбора галереи.
 */
/**
 * Отрисовывает нижний лист и обрабатывает действия внутри модального окна.
 *
 * @param onDismiss Колбэк закрытия модального компонента.
 * @param onCameraClick Колбэк выбора камеры в качестве источника изображения.
 * @param onGalleryClick Колбэк выбора галереи в качестве источника изображения.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPickerBottomSheet(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(32.dp)
                    .height(4.dp)
                    .background(Color(0xFFEBEBEB), RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = "Добавить фото",
                style = Title3Semibold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            PhotoOption(text = "Сделать снимок", icon = "📷", onClick = onCameraClick)
            Spacer(modifier = Modifier.height(16.dp))
            PhotoOption(text = "Выбрать из галереи", icon = "🖼️", onClick = onGalleryClick)
        }
    }
}

/**
 * Элемент списка выбора источника фото.
 *
 * @param text Отображаемый текст пункта.
 * @param icon Эмодзи-иконка пункта.
 * @param onClick Обработчик нажатия на пункт.
 */
/**
 * Отрисовывает элемент интерфейса и обрабатывает взаимодействие пользователя.
 *
 * @param text Текстовое содержимое элемента интерфейса.
 * @param icon Иконка или эмодзи, отображаемая рядом с текстом.
 * @param onClick Колбэк, вызываемый при нажатии пользователя.
 */
@Composable
fun PhotoOption(text: String, icon: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
        Text(text = text, style = TextRegular, color = Color.Black)
    }
}
