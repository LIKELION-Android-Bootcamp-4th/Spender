package com.e1i3.spender.feature.report.domain.model

data class Report(
    val id: Int,
    val month: String,
    val totalExpense: Int,
    val totalBudget: Int
)