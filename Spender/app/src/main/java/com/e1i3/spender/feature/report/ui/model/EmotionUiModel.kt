package com.e1i3.spender.feature.report.ui.model

import androidx.compose.ui.graphics.Color

data class EmotionUiModel(
    val label: String,
    val amount: Int,
    val percentage: Float,
    val color: Color
)