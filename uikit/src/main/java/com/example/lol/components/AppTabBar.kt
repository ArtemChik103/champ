package com.example.lol.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lol.ui.theme.AccentBlue

data class AppTabBarItem(
    val route: String,
    val title: String,
    @DrawableRes val iconRes: Int? = null
)

@Composable
fun AppTabBar(
    items: List<AppTabBarItem>,
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    testTagPrefix: String = "app_tab_bar"
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        contentColor = AccentBlue,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.testTag("${testTagPrefix}_${item.route}"),
                icon = {
                    if (item.iconRes != null) {
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        item.title,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal
                    )
                },
                selected = selectedRoute == item.route,
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentBlue,
                        selectedTextColor = AccentBlue,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Color(0xFFB8C1CC),
                        unselectedTextColor = Color(0xFFB8C1CC)
                    ),
                onClick = { onItemSelected(item.route) }
            )
        }
    }
}