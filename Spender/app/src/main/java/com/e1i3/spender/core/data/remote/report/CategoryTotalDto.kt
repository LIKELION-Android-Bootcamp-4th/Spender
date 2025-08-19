package com.e1i3.spender.core.data.remote.report

data class CategoryTotalDto(
    val categoryId: String,
    val categoryName: String,
    val totalPrice: Int,
    val colorHex: String
)
