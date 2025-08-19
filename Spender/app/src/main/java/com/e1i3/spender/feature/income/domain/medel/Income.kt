package com.e1i3.spender.feature.income.domain.medel

import java.util.Date

data class Income(
    val id: String = "",
    val amount: Long = 0,
    val title: String = "",
    val memo: String = "",
    val date: Date = Date(),
    val categoryId: String = ""
)