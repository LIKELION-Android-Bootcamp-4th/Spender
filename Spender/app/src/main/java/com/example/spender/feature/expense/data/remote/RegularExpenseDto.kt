package com.example.spender.feature.expense.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

data class RegularExpenseDto(
    val amount: Long = 0,
    val title: String = "",
    val memo: String = "",
    val categoryId: String = "",
    val first_payment_date: Timestamp = Timestamp.now(),
    val repeat_cycle: String = "MONTHLY",
    val day: Int = 28,
    val createdAt: FieldValue = FieldValue.serverTimestamp()
)