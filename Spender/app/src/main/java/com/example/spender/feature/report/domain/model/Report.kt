package com.example.spender.feature.report.domain.model

data class Report(
    val id: Int,
    val month: String,
    val totalExpense: Int,
    val totalBudget: Int
)