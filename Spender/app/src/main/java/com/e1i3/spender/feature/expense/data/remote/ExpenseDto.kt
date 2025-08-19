package com.e1i3.spender.feature.expense.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

data class ExpenseDto(
    val amount: Long = 0,
    val title: String = "",
    val memo: String = "",
    val date: Timestamp = Timestamp.now(),
    val categoryId: String = "",
    val emotion: String = "",
    val imageUrl: String? = null,
    val createdAt: FieldValue = FieldValue.serverTimestamp()
)