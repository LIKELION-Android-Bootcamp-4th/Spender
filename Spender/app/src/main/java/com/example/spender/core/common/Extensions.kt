package com.example.spender.core.common

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

//색상 변환
fun Color.toHexString(): String {
    val alpha = (this.alpha * 255).toInt()
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    return String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
}

// 문자열을 color 객체로 변환
fun String.toColor(): Color {
    return try {
        Color(this.toColorInt())
    } catch (e: IllegalArgumentException) {
        Color.Gray
    }
}