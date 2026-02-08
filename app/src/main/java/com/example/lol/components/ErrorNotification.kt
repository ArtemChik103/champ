package com.example.lol.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.lol.R
import com.example.lol.ui.theme.CaptionRegular
import com.example.lol.ui.theme.RedError
import kotlinx.coroutines.delay

/**
 * Всплывающее уведомление об ошибке с автозакрытием через 5 секунд.
 *
 * @param message Текст ошибки. Если `null`, уведомление не показывается.
 * @param onDismiss Вызывается при ручном или автоматическом закрытии.
 */
@Composable
fun ErrorNotification(message: String?, onDismiss: () -> Unit) {
        var isVisible by remember(message) { mutableStateOf(message != null) }

        LaunchedEffect(message) {
                if (message != null) {
                        isVisible = true
                        delay(5000)
                        isVisible = false
                        onDismiss()
                }
        }

        AnimatedVisibility(
                visible = isVisible && message != null,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier.zIndex(100f)
        ) {
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(top = 40.dp, start = 20.dp, end = 20.dp)
                ) {
                        Row(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .background(RedError, RoundedCornerShape(12.dp))
                                                .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Text(
                                        text = message ?: "",
                                        color = Color.White,
                                        style = CaptionRegular,
                                        modifier = Modifier.weight(1f)
                                )

                                Icon(
                                        painter = painterResource(id = R.drawable.icon_close),
                                        contentDescription = "Close",
                                        tint = Color.White,
                                        modifier =
                                                Modifier.size(20.dp).clickable {
                                                        isVisible = false
                                                        onDismiss()
                                                }
                                )
                        }
                }
        }
}
