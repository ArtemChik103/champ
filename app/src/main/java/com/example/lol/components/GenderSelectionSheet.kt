package com.example.lol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.Title3Semibold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectionSheet(
    onDismiss: () -> Unit,
    onGenderSelected: (String) -> Unit
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
                text = "Пол",
                style = Title3Semibold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            GenderOption(text = "Мужской", emoji = "👨", onClick = { onGenderSelected("Мужской") })
            Spacer(modifier = Modifier.height(16.dp))
            GenderOption(text = "Женский", emoji = "👩", onClick = { onGenderSelected("Женский") })
        }
    }
}

@Composable
fun GenderOption(text: String, emoji: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, style = Title3Semibold, modifier = Modifier.padding(end = 12.dp))
        Text(text = text, style = TextRegular, color = Color.Black)
    }
}
