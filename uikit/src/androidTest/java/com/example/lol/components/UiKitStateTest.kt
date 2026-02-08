package com.example.lol.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.lol.ui.theme.RedError
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class UiKitStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun inputErrorState_exposesErrorColorsAndMessage() {
        composeTestRule.setContent {
            AppTextField(
                value = "",
                onValueChange = {},
                label = "Email",
                placeholder = "name@mail.com",
                isError = true,
                errorMessage = "Поле обязательно",
                testTagPrefix = "input_error"
            )
        }

        composeTestRule.onNodeWithTag("input_error_error").assertTextContains("Поле обязательно")

        val containerConfig =
            composeTestRule.onNodeWithTag("input_error_container").fetchSemanticsNode().config
        val errorConfig = composeTestRule.onNodeWithTag("input_error_error").fetchSemanticsNode().config

        assertEquals(Color(0xFFFFF5F5).toArgb().toLong(), containerConfig[UiKitBackgroundColorKey])
        assertEquals(RedError.toArgb().toLong(), containerConfig[UiKitBorderColorKey])
        assertEquals(RedError.toArgb().toLong(), errorConfig[UiKitTextColorKey])
    }

    @Test
    fun selectWithoutIcon_clickOpensBottomSheetWithEmojiOptions() {
        composeTestRule.setContent {
            var selectedValue by mutableStateOf("")

            AppSelectField(
                value = selectedValue,
                label = "Выбор",
                placeholder = "Выберите",
                options = listOf("😀 Опция 1", "🚀 Опция 2"),
                showChevron = false,
                testTagPrefix = "select_no_icon",
                onOptionSelected = { selectedValue = it }
            )
        }

        composeTestRule.onNodeWithTag("select_no_icon_trigger").performClick()

        composeTestRule.onNodeWithTag("select_no_icon_sheet").assertTextContains("Выбор")
        composeTestRule.onNodeWithText("😀 Опция 1").assertTextContains("😀")
    }

    @Test
    fun chip_togglesBetweenSelectedAndNotSelected() {
        composeTestRule.setContent {
            var selected by mutableStateOf(false)

            AppChip(
                text = "Новинки",
                isSelected = selected,
                onClick = { selected = !selected },
                testTag = "chip_toggle"
            )
        }

        composeTestRule.onNodeWithTag("chip_toggle").assertIsNotSelected()
        composeTestRule.onNodeWithTag("chip_toggle").performClick()
        composeTestRule.onNodeWithTag("chip_toggle").assertIsSelected()
    }

    @Test
    fun selectField_selectingEmojiOptionUpdatesValue() {
        composeTestRule.setContent {
            var selectedValue by mutableStateOf("")

            AppSelectField(
                value = selectedValue,
                placeholder = "Выберите",
                options = listOf("🧪 Тест", "📦 Пакет"),
                testTagPrefix = "select_emoji",
                onOptionSelected = { selectedValue = it }
            )
        }

        composeTestRule.onNodeWithTag("select_emoji_trigger").performClick()
        composeTestRule.onNodeWithTag("select_emoji_option_0").performClick()

        composeTestRule.onNodeWithText("🧪 Тест").assertTextContains("Тест")
    }

    @Test
    fun tabBar_keepsSingleFocusedItem() {
        composeTestRule.setContent {
            var selectedRoute by mutableStateOf("main")
            val items =
                listOf(
                    AppTabBarItem(route = "main", title = "Main"),
                    AppTabBarItem(route = "catalog", title = "Catalog"),
                    AppTabBarItem(route = "profile", title = "Profile")
                )

            AppTabBar(
                items = items,
                selectedRoute = selectedRoute,
                testTagPrefix = "tab",
                onItemSelected = { selectedRoute = it }
            )
        }

        composeTestRule.onNodeWithTag("tab_main").assertIsSelected()
        composeTestRule.onNodeWithTag("tab_catalog").assertIsNotSelected()
        composeTestRule.onNodeWithTag("tab_profile").assertIsNotSelected()

        composeTestRule.onNodeWithTag("tab_profile").performClick()

        composeTestRule.onNodeWithTag("tab_profile").assertIsSelected()
        composeTestRule.onNodeWithTag("tab_main").assertIsNotSelected()
        composeTestRule.onNodeWithTag("tab_catalog").assertIsNotSelected()
    }
}
