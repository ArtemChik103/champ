package com.example.lol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lol.R
import com.example.lol.ui.theme.Title3Semibold

/**
 * Universal header component for the app.
 *
 * @param title Optional title text in the middle
 * @param showBackButton Whether to show a back chevron on the left
 * @param onBackClick Callback for the back button
 * @param trailingIcon Optional content on the right side
 * @param modifier Modifier for styling
 */
@Composable
fun AppHeader(
        title: String? = null,
        showBackButton: Boolean = false,
        onBackClick: () -> Unit = {},
        trailingIcon: @Composable (() -> Unit)? = null,
        modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier.fillMaxWidth().height(56.dp).padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left section: Back button or placeholder for symmetry
        Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.CenterStart) {
            if (showBackButton) {
                Box(
                        modifier =
                                Modifier.size(44.dp)
                                        .background(Color(0xFFF5F5F9), CircleShape)
                                        .clickable(onClick = onBackClick),
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            painter = painterResource(id = R.drawable.icon_chevron_left),
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Center section: Title
        if (title != null) {
            Text(text = title, style = Title3Semibold, color = Color.Black)
        }

        // Right section: Trailing icon or placeholder
        Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.CenterEnd) {
            if (trailingIcon != null) {
                trailingIcon()
            }
        }
    }
}
