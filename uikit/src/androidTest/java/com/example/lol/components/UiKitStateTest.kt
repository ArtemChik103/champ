package com.example.lol.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.lol.data.Product
import com.example.lol.ui.theme.RedError
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

// Содержит набор тестов для проверки поведения соответствующего модуля.
class UiKitStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Ожидаемый результат: ошибочный сценарий корректно возвращает состояние ошибки.
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

    // Ожидаемый результат: поведение в тестовом сценарии соответствует ожидаемому результату.
    @Test
    fun selectWithoutIcon_clickOpensBottomSheetWithEmojiOptions() {
        composeTestRule.setContent {
            var selectedValue by remember { mutableStateOf("") }

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

        composeTestRule.onNodeWithTag("select_no_icon_trigger", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()

        composeTestRule
            .onAllNodesWithTag("select_no_icon_option_0", useUnmergedTree = true)
            .assertCountEquals(1)
        composeTestRule
            .onAllNodesWithTag("select_no_icon_option_1", useUnmergedTree = true)
            .assertCountEquals(1)
    }

    // Ожидаемый результат: состояние корректно переключается после пользовательского действия.
    @Test
    fun chip_togglesBetweenSelectedAndNotSelected() {
        composeTestRule.setContent {
            var selected by remember { mutableStateOf(false) }

            AppChip(
                text = "Новинки",
                isSelected = selected,
                onClick = { selected = !selected },
                testTag = "chip_toggle"
            )
        }

        composeTestRule.onNodeWithTag("chip_toggle", useUnmergedTree = true).assertIsNotSelected()
        composeTestRule.onNodeWithTag("chip_toggle", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("chip_toggle", useUnmergedTree = true).assertIsSelected()
    }

    // Ожидаемый результат: после действия состояние обновляется ожидаемым образом.
    @Test
    fun selectField_selectingEmojiOptionUpdatesValue() {
        var selectedValueForAssertion = ""

        composeTestRule.setContent {
            var selectedValue by remember { mutableStateOf("") }

            AppSelectField(
                value = selectedValue,
                placeholder = "Выберите",
                options = listOf("🧪 Тест", "📦 Пакет"),
                testTagPrefix = "select_emoji",
                onOptionSelected = {
                    selectedValue = it
                    selectedValueForAssertion = it
                }
            )
        }

        composeTestRule.onNodeWithTag("select_emoji_trigger", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag("select_emoji_option_0", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()

        assertEquals("🧪 Тест", selectedValueForAssertion)
    }

    // Ожидаемый результат: поведение в тестовом сценарии соответствует ожидаемому результату.
    @Test
    fun tabBar_keepsSingleFocusedItem() {
        composeTestRule.setContent {
            var selectedRoute by remember { mutableStateOf("main") }
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

        composeTestRule.onNodeWithTag("tab_main", useUnmergedTree = true).assertIsSelected()
        composeTestRule.onNodeWithTag("tab_catalog", useUnmergedTree = true).assertIsNotSelected()
        composeTestRule.onNodeWithTag("tab_profile", useUnmergedTree = true).assertIsNotSelected()

        composeTestRule.onNodeWithTag("tab_profile", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("tab_profile", useUnmergedTree = true).assertIsSelected()
        composeTestRule.onNodeWithTag("tab_main", useUnmergedTree = true).assertIsNotSelected()
        composeTestRule.onNodeWithTag("tab_catalog", useUnmergedTree = true).assertIsNotSelected()
    }

    // Ожидаемый результат: фактический результат совпадает с ожидаемым значением.
    @Test
    fun productCard_addAndDeleteStates_triggerCorrectActions() {
        var addClicks = 0
        var removeClicks = 0

        val product =
            Product(
                id = 1,
                title = "Nike Air Max 270",
                description = "Great sneakers",
                price = 12990,
                category = "Sneakers",
                imageUrl = "https://example.com/image.png"
            )

        composeTestRule.setContent {
            var isInCart by remember { mutableStateOf(false) }

            ProductCard(
                product = product,
                onClick = {},
                onAddToCart = {
                    addClicks += 1
                    isInCart = true
                },
                onRemoveFromCart = {
                    removeClicks += 1
                    isInCart = false
                },
                isInCart = isInCart
            )
        }

        composeTestRule.onAllNodesWithText("Добавить", useUnmergedTree = true).assertCountEquals(1)
        composeTestRule.onAllNodesWithText("Убрать", useUnmergedTree = true).assertCountEquals(0)
        composeTestRule.onNodeWithText("Добавить", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()

        assertEquals(1, addClicks)
        assertEquals(0, removeClicks)

        composeTestRule.onAllNodesWithText("Убрать", useUnmergedTree = true).assertCountEquals(1)
        composeTestRule.onAllNodesWithText("Добавить", useUnmergedTree = true).assertCountEquals(0)
        composeTestRule.onNodeWithText("Убрать", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()

        assertEquals(1, addClicks)
        assertEquals(1, removeClicks)
        composeTestRule.onAllNodesWithText("Добавить", useUnmergedTree = true).assertCountEquals(1)
    }
}
