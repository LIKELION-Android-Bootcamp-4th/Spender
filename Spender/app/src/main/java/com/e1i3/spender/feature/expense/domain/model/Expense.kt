package com.e1i3.spender.feature.expense.domain.model

import java.util.Date

data class Expense(
    val id: String = "",
    val amount: Long = 0,
    val title: String = "",
    val memo: String = "",
    val date: Date = Date(),
    val categoryId: String = "",
    val emotion: String = "",
    val imageUrl: String? = null
)