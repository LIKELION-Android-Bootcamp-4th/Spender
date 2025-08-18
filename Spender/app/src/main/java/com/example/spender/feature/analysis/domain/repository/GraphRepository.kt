package com.example.spender.feature.analysis.domain.repository

import com.example.spender.core.data.remote.expense.ExpenseDto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class GraphRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getMaxExpense(year: Int, month: Int): ExpenseDto? {
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

        return try {
            val expenseRef = firestore.collection("users").document(uid!!).collection("expenses")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("date", Timestamp(startOfMonth.time))
                .whereLessThanOrEqualTo("date", Timestamp(endOfMonth.time))
                .get()
                .await()
            expenseRef.documents.mapNotNull { ExpenseDto(
                id = it.id,
                amount = -(it["amount"]?.toString()?.toInt() ?: 0),
                title = it["title"]?.toString() ?: "",
                date = Timestamp.now(),
                categoryId = it["categoryId"]?.toString() ?: "",
                createdAt = Timestamp.now()
            ) }.minByOrNull { it.amount }
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getDailyTotalList(year: Int, month: Int): MutableMap<String, Int> {
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
            val expenseRef = firestore.collection("users").document(uid!!).collection("expenses")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("date", Timestamp(startOfMonth.time))
                .whereLessThanOrEqualTo("date", Timestamp(endOfMonth.time))
                .get()
                .await()
            expenseRef.documents.forEach { document ->
                val data = document.data ?: return@forEach
                val amount = data["amount"]?.toString()?.toInt() ?: 0
                val date = data["date"] as? Timestamp ?: Timestamp.now()

                val stamp = Calendar.getInstance().apply { time = date.toDate() }
                val day = "%02d".format(stamp.get(Calendar.DAY_OF_MONTH))
                dailySum[day] = dailySum.getOrDefault(day, 0) + amount
            }
            return dailySum
        } catch (e: Exception) {
        }
        return dailySum
    }
}