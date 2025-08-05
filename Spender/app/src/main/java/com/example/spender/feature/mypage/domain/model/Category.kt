package com.example.spender.feature.mypage.domain.model

import androidx.compose.ui.graphics.Color

data class Category(
    val id: String = "",
    val name: String,
    val color: Color? = null
)