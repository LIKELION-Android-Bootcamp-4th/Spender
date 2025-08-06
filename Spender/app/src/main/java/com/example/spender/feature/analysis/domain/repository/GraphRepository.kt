package com.example.spender.feature.analysis.domain.repository

import android.util.Log
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import java.util.Calendar

class GraphRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun getMaxExpense(year: Int, month: Int): ExpenseDto? {
        val uid = auth.currentUser?.uid

        val startOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        var expense: ExpenseDto? = null

        try {
            val expenseRef = firestore.collection("users").document(uid!!).collection("expense")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfMonth.time))
                .whereLessThanOrEqualTo("createdAt", Timestamp(endOfMonth.time))

            expenseRef.get().addOnSuccessListener { document ->
                expense = document.documents
                    .mapNotNull { it.toObject(ExpenseDto::class.java) }
                    .maxByOrNull { it.amount }
            }
        } catch (e: Exception) {
            Log.d("Analysis / Max Expense", "Max Expense error")
        }

        return expense
    }

    fun getDailyTotalList(year: Int, month: Int): MutableMap<String, Int> {
        val uid = auth.currentUser?.uid

        val startOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        val dailySum = mutableMapOf<String, Int>()

        try {
            val expenseRef = firestore.collection("users").document(uid!!).collection("expense")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfMonth.time))
                .whereLessThanOrEqualTo("createdAt", Timestamp(endOfMonth.time))

            expenseRef.get().addOnSuccessListener { document ->
                for (doc in document) {
                    val data = doc.data ?: continue
                    val amount = data["amount"] as? Int ?: continue
                    val createdAt = data["createdAt"] as? Timestamp ?: continue

                    val stamp = Calendar.getInstance().apply { time = createdAt.toDate() }
                    val day = "%02d".format(stamp.get(Calendar.DAY_OF_MONTH))
                    dailySum[day] = dailySum.getOrDefault(day, 0) + amount
                }
            }
        } catch (e: Exception) {
            Log.d("Analysis / Daily Sum List", "Daily expense error")
        }

        return dailySum
    }
}