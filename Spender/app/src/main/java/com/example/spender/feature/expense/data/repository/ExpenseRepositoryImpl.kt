package com.example.spender.feature.expense.data.repository

import android.util.Log
import com.example.spender.feature.expense.data.remote.ExpenseDto
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject

class ExpenseRepositoryImpl @Inject constructor() : ExpenseRepository {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    override fun addExpense(userId: String, expense: ExpenseDto) {
        usersCollection.document(userId).collection("expenses")
            .add(expense)
            .addOnSuccessListener {
                Log.d("Firestore", "Expense 추가 성공!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "ㅠㅠ Expense 추가 실패", e)
            }
    }
}