package com.e1i3.spender.core.data.remote.expense

import com.google.firebase.Timestamp

data class RegularExpenseDto(
    val id: String,
    val amount: Int,
    val memo: String = "",
    val title: String,
    val day: Int,
    val firstPaymentDate: Timestamp,
    val repeatCycle: String,
    val createdAt: Timestamp,
)
