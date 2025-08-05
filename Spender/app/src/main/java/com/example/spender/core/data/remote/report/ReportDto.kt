package com.example.spender.core.data.remote.report

import com.google.firebase.Timestamp

data class ReportDto (
    val totalBudget: Int,
    val totalExpense: Int,
    val feedBack: String = "",
    val month: String,
    val byCategory: Array<CategoryTotalDto>,
    val byEmotion: Array<EmotionTotalDto>,
    val createdAt: Timestamp
)