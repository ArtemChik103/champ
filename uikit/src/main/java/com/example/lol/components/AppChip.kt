package com.example.lol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.lol.ui.theme.AccentBlue
import com.example.lol.ui.theme.ChipInactiveBg
import com.example.lol.ui.theme.TextGray
import com.example.lol.ui.theme.TextMedium

/**
 * Отрисовывает composable-компонент в соответствии с переданным состоянием.
 *
 * @param text Текстовое содержимое элемента интерфейса.
 * @param isSelected Флаг выбранного состояния элемента интерфейса.
 * @param onClick Колбэк, вызываемый при нажатии пользователя.
 * @param modifier Внешний `Modifier` для настройки размеров, отступов и поведения компонента.
 * @param testTag Тестовый тег для поиска компонента в UI-тестах.
 */
@Composable
fun AppChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "app_chip"
) {
    Box(
        modifier =
            modifier
                .background(
                    color = if (isSelected) AccentBlue else ChipInactiveBg,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .testTag(testTag)
                .semantics {
                    selected = isSelected
                },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) androidx.compose.ui.graphics.Color.White else TextGray,
            style = TextMedium
        )
    }
}
