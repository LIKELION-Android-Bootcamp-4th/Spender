package com.example.spender.core.data.remote.report

data class CategoryTotalDto(
    val categoryId: String, // TODO : 나중에 category에서 id 가져오는걸로 수정(아마 int)
    val totalPrice: Int
)
