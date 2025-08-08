package com.example.spender.feature.expense.data.repository

import com.example.spender.feature.expense.data.remote.RegularExpenseDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegularExpenseRepository @Inject constructor() {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    suspend fun addRegularExpense(userId: String, expense: RegularExpenseDto): Boolean {
        return try {
            usersCollection.document(userId).collection("regular_expenses").add(expense).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}