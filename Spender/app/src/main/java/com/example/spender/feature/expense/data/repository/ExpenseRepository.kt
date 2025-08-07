package com.example.spender.feature.expense.data.repository

import android.util.Log
import com.example.spender.feature.expense.data.remote.ExpenseDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ExpenseRepository @Inject constructor() {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    suspend fun addExpense(userId: String, expense: ExpenseDto): Boolean {
        return try {
            usersCollection.document(userId).collection("expenses").add(expense).await()
            true
        } catch (e: Exception) {
            Log.w("Firestore", "지출 등록 실패", e)
            false
        }
    }
}