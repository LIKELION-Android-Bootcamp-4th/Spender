package com.example.spender.feature.analysis.domain.repository

import android.util.Log
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import java.util.Calendar

class CalendarRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun getExpenseList(year: Int, month: Int): MutableList<ExpenseDto> {
        val uid = auth.currentUser?.uid
        val expenseList = mutableListOf<ExpenseDto>()

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

        try {
            val ref = firestore.collection("users").document(uid!!).collection("expense")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfMonth.time))
                .whereLessThanOrEqualTo("createdAt", Timestamp(endOfMonth.time))
            ref.get().addOnSuccessListener { document ->
                for (doc in document) {
                    val data = doc.data
                    val expense = ExpenseDto(
                        amount = data["amount"]?.toString()?.toInt() ?: 0,
                        memo = data["memo"]?.toString() ?: "",
                        title = data["title"]?.toString() ?: "",
                        date = data["date"] as? Timestamp ?: Timestamp.now(),
                        receiptUrl = data["receiptUrl"]?.toString() ?: "",
                        emotionId = data["emotionId"]?.toString() ?: "",
                        categoryId = data["categoryId"]?.toString() ?: "",
                        createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                    )
                    expenseList.add(expense)
                }
            }
        } catch (e: Exception) {
            Log.d("Analysis / ExpenseList", "Expense list error")
        }

        return expenseList
    }

    fun getIncomeList(year: Int, month: Int): MutableList<ExpenseDto> {
        val uid = auth.currentUser?.uid
        val incomeList = mutableListOf<ExpenseDto>()

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

        try {
            val ref = firestore.collection("users").document(uid!!).collection("incomes")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfMonth.time))
                .whereLessThanOrEqualTo("createdAt", Timestamp(endOfMonth.time))
            ref.get().addOnSuccessListener { document ->
                for (doc in document) {
                    val data = doc.data
                    val expense = ExpenseDto(
                        amount = data["amount"]?.toString()?.toInt() ?: 0,
                        memo = data["memo"]?.toString() ?: "",
                        title = data["title"]?.toString() ?: "",
                        date = data["date"] as? Timestamp ?: Timestamp.now(),
                        receiptUrl = "",
                        emotionId = "",
                        categoryId = data["categoryId"]?.toString() ?: "",
                        createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                    )
                    incomeList.add(expense)
                }
            }
        } catch (e: Exception) {
            Log.d("Analysis / IncomeList", "Income list error")
        }

        return incomeList
    }

    fun getDailyList(year: Int, month: Int, day: Int): MutableList<ExpenseDto> {
        val uid = auth.currentUser?.uid
        val dataList = mutableListOf<ExpenseDto>()

        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endOfDay = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        try {
            val expenseRef = firestore.collection("users").document(uid!!).collection("expenses")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfDay.time))
                .whereLessThanOrEqualTo("createdAt", Timestamp(endOfDay.time))
            expenseRef.get().addOnSuccessListener { document ->
                for (doc in document) {
                    val data = doc.data
                    val expense = ExpenseDto(
                        amount = data["amount"]?.toString()?.toInt() ?: 0,
                        memo = data["memo"]?.toString() ?: "",
                        title = data["title"]?.toString() ?: "",
                        date = data["date"] as? Timestamp ?: Timestamp.now(),
                        receiptUrl = "",
                        emotionId = "",
                        categoryId = data["categoryId"]?.toString() ?: "",
                        createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                    )
                    dataList.add(expense)
                }
            }
        } catch (e: Exception) {
            Log.d("Analysis / Expense Daily List", "Expense list error")
        }

        try {
            val incomeRef = firestore.collection("users").document(uid!!).collection("incomes")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfDay.time))
                .whereLessThanOrEqualTo("createdAt", Timestamp(endOfDay.time))
            incomeRef.get().addOnSuccessListener { document ->
                for (doc in document) {
                    val data = doc.data
                    val expense = ExpenseDto(
                        amount = data["amount"]?.toString()?.toInt() ?: 0,
                        memo = data["memo"]?.toString() ?: "",
                        title = data["title"]?.toString() ?: "",
                        date = data["date"] as? Timestamp ?: Timestamp.now(),
                        receiptUrl = "",
                        emotionId = "",
                        categoryId = data["categoryId"]?.toString() ?: "",
                        createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                    )
                    dataList.add(expense)
                }
            }
        } catch (e: Exception) {
            Log.d("Analysis / Income Daily List", "Income list error")
        }

        dataList.sortByDescending { it.createdAt }
        return dataList
    }
}