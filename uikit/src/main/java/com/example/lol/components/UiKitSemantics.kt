package com.example.lol.components

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

val UiKitBackgroundColorKey = SemanticsPropertyKey<Long>("uikit_background_color")
var SemanticsPropertyReceiver.uiKitBackgroundColor by UiKitBackgroundColorKey

val UiKitBorderColorKey = SemanticsPropertyKey<Long>("uikit_border_color")
var SemanticsPropertyReceiver.uiKitBorderColor by UiKitBorderColorKey

val UiKitTextColorKey = SemanticsPropertyKey<Long>("uikit_text_color")
var SemanticsPropertyReceiver.uiKitTextColor by UiKitTextColorKey