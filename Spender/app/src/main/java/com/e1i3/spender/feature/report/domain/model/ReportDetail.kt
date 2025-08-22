package com.e1i3.spender.feature.report.domain.model

data class ReportDetail(
    val month: String,
    val tier: Int,
    val totalExpense: Int,
    val totalBudget: Int,
    val feedback: String,
    val byCategory: List<CategoryTotal>,
    val byEmotion: List<EmotionTotal>
)