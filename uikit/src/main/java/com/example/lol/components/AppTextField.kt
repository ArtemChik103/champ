package com.example.lol.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lol.ui.theme.InputBg
import com.example.lol.ui.theme.InputStroke
import com.example.lol.ui.theme.TextGray
import com.example.lol.ui.theme.TextRegular
import com.example.lol.ui.theme.CaptionRegular
import com.example.lol.ui.theme.Roboto

/**
 * Custom text field component for the app with consistent styling.
 * 
 * @param value Current text value
 * @param onValueChange Callback when text changes
 * @param label Optional label above the field
 * @param placeholder Placeholder text when empty
 * @param modifier Modifier for styling
 * @param visualTransformation Transformation for password etc.
 * @param keyboardOptions Keyboard configuration
 * @param trailingIcon Icon at the end
 * @param leadingIcon Icon at the start
 * @param singleLine Whether to restrict to single line
 * @param isError Whether to show error state
 * @param errorMessage Text to display when in error state
 * @param testTagPrefix Prefix for UI-test tags
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    testTagPrefix: String = "app_text_field"
) {
    val backgroundColor = if (isError) Color(0xFFFFF5F5) else InputBg
    val borderColor = if (isError) com.example.lol.ui.theme.RedError else InputStroke
    val errorColor = com.example.lol.ui.theme.RedError

    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = CaptionRegular,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(backgroundColor, RoundedCornerShape(10.dp))
                .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                .testTag("${testTagPrefix}_container")
                .semantics {
                    uiKitBackgroundColor = backgroundColor.toArgb().toLong()
                    uiKitBorderColor = borderColor.toArgb().toLong()
                },
            textStyle = TextRegular.copy(
                color = Color.Black
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            cursorBrush = SolidColor(Color.Black),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color(0xFFBFC7D1),
                                style = TextRegular
                            )
                        }
                        innerTextField()
                    }
                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(12.dp))
                        trailingIcon()
                    }
                }
            }
        )
        if (isError && errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = CaptionRegular,
                color = errorColor,
                modifier = Modifier
                    .testTag("${testTagPrefix}_error")
                    .semantics { uiKitTextColor = errorColor.toArgb().toLong() },
                fontSize = 12.sp
            )
        }
    }
}
