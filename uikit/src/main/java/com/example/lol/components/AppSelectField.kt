package com.example.lol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lol.uikit.R
import com.example.lol.ui.theme.CaptionRegular
import com.example.lol.ui.theme.InputBg
import com.example.lol.ui.theme.InputStroke
import com.example.lol.ui.theme.RedError
import com.example.lol.ui.theme.TextGray
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.Title3Semibold

/**
 * Поле выбора значения из списка через ModalBottomSheet.
 *
 * @param value Текущее выбранное значение.
 * @param label Подпись над полем.
 * @param placeholder Текст-заполнитель, когда значение не выбрано.
 * @param options Список доступных вариантов.
 * @param onOptionSelected Колбэк выбора варианта.
 * @param showChevron Показывать ли иконку стрелки в поле.
 * @param isError Признак ошибки поля.
 * @param errorMessage Сообщение ошибки под полем.
 * @param testTagPrefix Префикс тестовых тегов.
 * @param modifier Модификатор контейнера.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectField(
        value: String,
        label: String? = null,
        placeholder: String = "",
        options: List<String>,
        onOptionSelected: (String) -> Unit,
        showChevron: Boolean = true,
        isError: Boolean = false,
        errorMessage: String? = null,
        testTagPrefix: String = "app_select_field",
        modifier: Modifier = Modifier
) {
    var isSheetVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val borderColor = if (isError) RedError else InputStroke
    val backgroundColor = if (isError) Color(0xFFFFF5F5) else InputBg

    Column(modifier = modifier) {
        if (label != null) {
            Text(text = label, style = CaptionRegular, color = TextGray)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .height(48.dp)
                                .background(backgroundColor, RoundedCornerShape(10.dp))
                                .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                                .clickable { isSheetVisible = true }
                                .padding(horizontal = 16.dp)
                                .testTag("${testTagPrefix}_trigger"),
                contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                        text = if (value.isEmpty()) placeholder else value,
                        color = if (value.isEmpty()) Color(0xFFBFC7D1) else Color.Black,
                        style = TextRegular,
                        modifier = Modifier.weight(1f)
                )
                if (showChevron) {
                    Icon(
                            painter = painterResource(id = R.drawable.icon_chevron_down),
                            contentDescription = "Open options",
                            tint = Color(0xFFBFC7D1),
                            modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (isError && !errorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = errorMessage, style = CaptionRegular, color = RedError)
        }
    }

    if (isSheetVisible) {
        ModalBottomSheet(
                onDismissRequest = { isSheetVisible = false },
                sheetState = sheetState,
                containerColor = Color.White,
                modifier = Modifier.testTag("${testTagPrefix}_sheet")
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                if (!label.isNullOrBlank()) {
                    Text(
                            text = label,
                            style = Title3Semibold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                options.forEachIndexed { index, option ->
                    Row(
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .clickable {
                                                onOptionSelected(option)
                                                isSheetVisible = false
                                            }
                                            .padding(vertical = 14.dp)
                                            .testTag("${testTagPrefix}_option_$index"),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = option, style = TextRegular, color = Color.Black)
                    }
                }
            }
        }
    }
}
