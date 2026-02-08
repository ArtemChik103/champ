package com.example.lol.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.lol.R

/** Reusable search bar component that wraps AppTextField with a search icon. */
@Composable
fun SearchBar(
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: String = "Поиск",
        modifier: Modifier = Modifier
) {
    AppTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            modifier = modifier,
            leadingIcon = {
                Icon(
                        painter = painterResource(id = R.drawable.icon_search),
                        contentDescription = "Search",
                        tint = Color(0xFF939396)
                )
            }
    )
}
