package com.example.spender.feature.report.domain.model

import androidx.compose.ui.graphics.Color

data class CategoryTotal(
    val categoryId: String,
    val categoryName: String,
    val totalPrice: Int,
    val color: Color
)