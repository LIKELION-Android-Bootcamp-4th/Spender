package com.example.spender.core.data.remote.report

data class ReportListDto(
    val month: String,
    val totalExpense: Int,
    val totalBudget: Int
)