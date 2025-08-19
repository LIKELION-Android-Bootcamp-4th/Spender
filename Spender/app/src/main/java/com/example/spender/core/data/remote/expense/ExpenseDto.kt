package com.example.spender.core.data.remote.expense

import com.google.firebase.Timestamp

data class ExpenseDto(
    val id: String = "",
    val amount: Int,
    val memo: String = "",
    val title: String,
    val date: Timestamp,
    val receiptUrl: String = "",
    val emotionId: String = "",
    val categoryId: String,
    val createdAt: Timestamp
)
