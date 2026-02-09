package com.example.lol.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// Пока используются системные шрифты как безопасный fallback.
// Пока используются системные шрифты как безопасный резервный вариант.
val Roboto = FontFamily.Default
val RobotoFlex = FontFamily.Default
val SFProDisplay = FontFamily.Default

val Title1Semibold =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.0033.em
        )

val Title1Bold =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.0033.em
        )

val Title1ExtraBold =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.0033.em
        )

val Title2Regular =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.0038.em
        )

val Title2Semibold =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.0038.em
        )

val Title2ExtraBold =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.0038.em
        )

val Title2Bold =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.0038.em
        )

val Title3Regular =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                lineHeight = 24.sp
        )

val Title3Medium =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
                lineHeight = 24.sp
        )

val Title3Semibold =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                lineHeight = 24.sp
        )

val HeadlineRegular =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = (-0.0032).em
        )

val HeadlineMedium =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = (-0.0032).em
        )

val TextRegular =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                lineHeight = 20.sp
        )

val TextMedium =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 20.sp
        )

val CaptionRegular =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp
        )

val CaptionSemibold =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp
        )

val Caption2Regular =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp
        )

val Caption2Bold =
        TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 16.sp
        )

val TypographyTitle =
        TextStyle(
                fontFamily = RobotoFlex,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                lineHeight = 48.sp,
                letterSpacing = 0.01.em
        )

val AccentStyle =
        TextStyle(
                fontFamily = RobotoFlex,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 16.sp
        )

val Typography =
        Typography(
                displayLarge = TypographyTitle,
                headlineLarge = Title1ExtraBold,
                headlineMedium = Title1Semibold,
                titleLarge = Title2Semibold,
                titleMedium = Title3Semibold,
                titleSmall = Title3Medium,
                bodyLarge = HeadlineRegular,
                bodyMedium = TextRegular,
                bodySmall = CaptionRegular,
                labelLarge = HeadlineMedium,
                labelMedium = TextMedium,
                labelSmall = Caption2Regular
        )
