package com.example.spender.core.data.remote.report

data class ReportDetailDto (
    val totalBudget: Int,
    val totalExpense: Int,
    val feedback: String = "",
    val month: String,
    val byCategory: List<CategoryTotalDto>,
    val byEmotion: List<EmotionTotalDto>,
//    val createdAt: Timestamp
)