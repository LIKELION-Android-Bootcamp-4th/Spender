package com.example.spender.feature.expense.domain.model

import java.util.Date

data class RegularExpense(
    val id: String = "",
    val amount: Long = 0,
    val title: String = "",
    val memo: String = "",
    val categoryId: String = "",
    val first_payment_date: Date = Date(),
    val repeatCycle: String = "MONTHLY", // "매월"
    val day: Int = 28
)