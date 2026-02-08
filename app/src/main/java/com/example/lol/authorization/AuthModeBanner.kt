package com.example.lol.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lol.ui.theme.CaptionRegular

@Composable
fun AuthModeBanner(modifier: Modifier = Modifier) {
    if (!AuthRepositoryProvider.isMockMode()) return

    Box(
        modifier =
            modifier
                .background(Color(0xFFFFF4D6), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "MockAuth mode",
            style = CaptionRegular,
            color = Color(0xFF7A5400)
        )
    }
}