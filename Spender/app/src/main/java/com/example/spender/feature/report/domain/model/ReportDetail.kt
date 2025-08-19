package com.example.spender.feature.report.domain.model

data class ReportDetail(
    val month: String,
    val totalExpense: Int,
    val totalBudget: Int,
    val feedback: String,
    val byCategory: List<CategoryTotal>,
    val byEmotion: List<EmotionTotal>
)