package com.example.spender.feature.income.data.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.Timestamp

data class IncomeDto(
    val amount: Long = 0,
    val title: String = "",
    val memo: String = "",
    val date: Timestamp = Timestamp.now(),
    val categoryId: String = "",
    val createdAt: FieldValue = FieldValue.serverTimestamp()
)