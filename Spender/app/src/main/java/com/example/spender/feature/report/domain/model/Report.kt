package com.example.spender.feature.report.domain.model

data class Report(
    val id: Int,
    val yearMonth: String,
    val totalExpense: Int,
    val budget: Int
)