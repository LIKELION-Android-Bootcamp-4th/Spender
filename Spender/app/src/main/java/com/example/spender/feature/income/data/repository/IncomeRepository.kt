package com.example.spender.feature.income.data.repository

import android.util.Log
import com.example.spender.feature.income.data.remote.IncomeDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class IncomeRepository @Inject constructor() {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    suspend fun addIncome(userId: String, income: IncomeDto): Boolean {
        return try {
            usersCollection.document(userId).collection("incomes").add(income).await()
            true
        } catch (e: Exception) {
            Log.w("Firestore", "수입 등록 실패", e)
            false
        }
    }
}