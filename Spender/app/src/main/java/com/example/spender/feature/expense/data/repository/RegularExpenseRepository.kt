package com.example.spender.feature.expense.data.repository

import com.example.spender.feature.expense.data.remote.RegularExpenseDto
import com.example.spender.feature.expense.domain.model.RegularExpense
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

    suspend fun getRegularExpenseById(userId: String, regularExpenseId: String): RegularExpense? {
        return try {
            val document = usersCollection.document(userId).collection("regular_expenses").document(regularExpenseId).get().await()
            document.toObject(RegularExpense::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateRegularExpense(userId: String, regularExpenseId: String, dto: RegularExpenseDto): Boolean {
        return try {
            usersCollection.document(userId).collection("regular_expenses").document(regularExpenseId).set(dto).await()
            true
        } catch (e: Exception) { false }
    }

    suspend fun deleteRegularExpense(userId: String, regularExpenseId: String): Boolean {
        return try {
            usersCollection.document(userId).collection("regular_expenses").document(regularExpenseId).delete().await()
            true
        } catch (e: Exception) { false }
    }

    //정기 지출 목록 불러오기
    fun getRegularExpenses(
        userId: String,
        onResult: (List<RegularExpense>) -> Unit
    ) {
        usersCollection.document(userId).collection("regular_expenses")
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onResult(emptyList())
                    return@addSnapshotListener
                }
                val expenses = snapshot?.documents?.mapNotNull {
                    it.toObject(RegularExpense::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onResult(expenses)
            }
    }
}