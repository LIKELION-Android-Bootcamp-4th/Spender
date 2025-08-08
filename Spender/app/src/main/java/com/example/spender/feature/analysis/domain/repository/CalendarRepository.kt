package com.example.spender.feature.analysis.domain.repository

import android.util.Log
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class CalendarRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getExpenseList(year: Int, month: Int): MutableList<ExpenseDto> {
        val uid = auth.currentUser?.uid
        val expenseList = mutableListOf<ExpenseDto>()

        val startOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month-1)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month-1)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        return try {
            val ref = firestore.collection("users").document(uid!!).collection("expenses")
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfMonth.time))
                .whereLessThanOrEqualTo("createdAt", Timestamp(endOfMonth.time))
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            ref.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                ExpenseDto(
                    amount = data["amount"]?.toString()?.toIntOrNull() ?: 0,
                    memo = data["memo"]?.toString() ?: "",
                    title = data["title"]?.toString() ?: "",
                    date = data["date"] as? Timestamp ?: Timestamp.now(),
                    categoryId = data["categoryId"]?.toString() ?: "",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
            }.toMutableList()
        } catch (e: Exception) {
            Log.d("Analysis / ExpenseList", "Expense list error")
            mutableListOf()
        }
    }

    suspend fun getIncomeList(year: Int, month: Int): MutableList<ExpenseDto> {
        val uid = auth.currentUser?.uid
        val incomeList = mutableListOf<ExpenseDto>()

        val startOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month-1)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endOfMonth = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month-1)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        return try {
            val ref = firestore.collection("users").document(uid!!).collection("incomes")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfMonth.time))
                .whereLessThanOrEqualTo("createdAt", Timestamp(endOfMonth.time))
                .get()
                .await()

            ref.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                ExpenseDto(
                    amount = data["amount"]?.toString()?.toIntOrNull() ?: 0,
                    memo = data["memo"]?.toString() ?: "",
                    title = data["title"]?.toString() ?: "",
                    date = data["date"] as? Timestamp ?: Timestamp.now(),
                    categoryId = data["categoryId"]?.toString() ?: "",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
            }.toMutableList()
        } catch (e: Exception) {
            Log.d("Analysis / IncomeList", "Income list error")
            mutableListOf()
        }
    }

    suspend fun getDailyList(year: Int, month: Int, day: Int): MutableList<ExpenseDto> {
        val uid = auth.currentUser?.uid
        var dataList = mutableListOf<ExpenseDto>()

        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month-1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endOfDay = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month-1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        dataList = try {
            val expenseRef = firestore.collection("users").document(uid!!).collection("expenses")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("date", Timestamp(startOfDay.time))
                .whereLessThanOrEqualTo("date", Timestamp(endOfDay.time))
                .get()
                .await()
            expenseRef.documents.mapNotNull { data ->
                ExpenseDto(
                    amount = -(data["amount"]?.toString()?.toInt() ?: 0),
                    memo = data["memo"]?.toString() ?: "",
                    title = data["title"]?.toString() ?: "",
                    date = data["date"] as? Timestamp ?: Timestamp.now(),
                    receiptUrl = "",
                    emotionId = "",
                    categoryId = data["categoryId"]?.toString() ?: "",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
            }.toMutableList()
        } catch (e: Exception) {
            Log.d("Analysis / Expense Daily List", "Expense list error")
            mutableListOf()
        }

        try {
            val incomeRef = firestore.collection("users").document(uid!!).collection("incomes")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfDay.time))
                .whereLessThanOrEqualTo("createdAt", Timestamp(endOfDay.time))
                .get()
                .await()
            dataList.addAll(incomeRef.documents.mapNotNull { doc ->
                ExpenseDto(
                    amount = doc["amount"]?.toString()?.toInt() ?: 0,
                    memo = doc["memo"]?.toString() ?: "",
                    title = doc["title"]?.toString() ?: "",
                    date = doc["date"] as? Timestamp ?: Timestamp.now(),
                    receiptUrl = "",
                    emotionId = "",
                    categoryId = doc["categoryId"]?.toString() ?: "",
                    createdAt = doc["createdAt"] as? Timestamp ?: Timestamp.now()
                )
            }.toMutableList())
        } catch (e: Exception) {
            Log.d("Analysis / Income Daily List", "Income list error")
        }

        dataList.sortByDescending { it.createdAt }
        return dataList
    }
}