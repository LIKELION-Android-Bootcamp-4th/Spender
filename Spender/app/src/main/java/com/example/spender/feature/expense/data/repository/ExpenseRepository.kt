package com.example.spender.feature.expense.data.repository

import com.example.spender.feature.expense.data.remote.ExpenseDto
import com.example.spender.feature.expense.domain.model.Expense
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
            false
        }
    }

    suspend fun getExpenseById(userId: String, expenseId: String): Expense? {
        return try {
            val document = usersCollection.document(userId).collection("expenses").document(expenseId).get().await()
            document.toObject(Expense::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateExpense(userId: String, expenseId: String, expenseDto: ExpenseDto): Boolean {
        return try {
            usersCollection.document(userId).collection("expenses").document(expenseId).set(expenseDto).await()
            true
        } catch (e: Exception) { false }
    }

    suspend fun deleteExpense(userId: String, expenseId: String): Boolean {
        return try {
            usersCollection.document(userId).collection("expenses").document(expenseId).delete().await()
            true
        } catch (e: Exception) { false }
    }
}