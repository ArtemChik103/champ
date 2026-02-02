package com.example.lol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lol.R
import com.example.lol.ui.theme.CaptionRegular
import com.example.lol.ui.theme.InputBg
import com.example.lol.ui.theme.InputStroke
import com.example.lol.ui.theme.TextGray
import com.example.lol.ui.theme.TextRegular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectField(
        value: String,
        label: String? = null,
        placeholder: String = "",
        options: List<String>,
        onOptionSelected: (String) -> Unit,
        modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (label != null) {
            Text(text = label, style = CaptionRegular, color = TextGray)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .height(48.dp)
                                .background(InputBg, RoundedCornerShape(10.dp))
                                .border(1.dp, InputStroke, RoundedCornerShape(10.dp))
                                .clickable { expanded = true }
                                .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                        text = if (value.isEmpty()) placeholder else value,
                        color = if (value.isEmpty()) Color(0xFFBFC7D1) else Color.Black,
                        style = TextRegular,
                        modifier = Modifier.weight(1f)
                )
                Icon(
                        painter = painterResource(id = R.drawable.icon_chevron_down),
                        contentDescription = "Dropdown",
                        tint = Color(0xFFBFC7D1),
                        modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White).fillMaxWidth(0.8f)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                            text = { Text(option, style = TextRegular) },
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                            }
                    )
                }
            }
        }
    }
}
