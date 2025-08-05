package com.example.spender.core.data.service

import android.util.Log
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import java.util.Calendar

// get

// 한 달 동안의 지출 내역
fun getExpenseList(year: Int, month: Int): MutableList<ExpenseDto> {
    val uid = getFirebaseAuth()
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
        val ref = getFirebaseRef().document(uid!!).collection("expense")
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

// 한 달 동안의 수입 내역
fun getIncomeList(year: Int, month: Int): MutableList<ExpenseDto> {
    val uid = getFirebaseAuth()
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
        val ref = getFirebaseRef().document(uid!!).collection("incomes")
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

// 하루 동안의 수입/지출 내역
fun getDailyList(year: Int, month: Int, day: Int): MutableList<ExpenseDto> {
    val uid = getFirebaseAuth()
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
        val expenseRef = getFirebaseRef().document(uid!!).collection("expenses")
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
        val incomeRef = getFirebaseRef().document(uid!!).collection("incomes")
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

// 일당 소비 총액 반환 (단, 수입을 신경 쓰지는 않음)
fun getDailyTotalList(year: Int, month: Int): MutableMap<String, Int> {
    val uid = getFirebaseAuth()

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
        val expenseRef = getFirebaseRef().document(uid!!).collection("expense")
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

fun getMaxExpense(year: Int, month: Int): ExpenseDto? {
    val uid = getFirebaseAuth()

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
        val expenseRef = getFirebaseRef().document(uid!!).collection("expense")
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