package com.example.spender.core.data.service

import android.util.Log
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import java.util.Calendar

// 총 지출 금액을 가져옴.
fun getTotalExpense(): Int {
    val uid = getFirebaseAuth()
    var total = 0
    try {
        val ref = getFirebaseRef().document(uid!!).collection("expense")
        ref.get().addOnSuccessListener { document ->
            for (doc in document.documents) {
                total += doc.data?.get("amount")?.toString()?.toInt() ?: 0
            }
        }
    } catch (e: Exception) {
        Log.d("Home / TotalBudget", "total budget error")
        return 0
    }
    return total
}

// 예산 소진 비율'만' 가져옴. 비율이 위험한지 아닌지는 따로 비즈니스 로직 필요
fun getExpenseRate(): Int {
    val uid = getFirebaseAuth()

    val startOfMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val endOfMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }

    var budget = 1
    var expense = 0

    try {
        val budgetRef = getFirebaseRef().document(uid!!).collection("budgets")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
        val expenseRef = getFirebaseRef().document(uid).collection("expenses")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfMonth.time))
            .whereLessThanOrEqualTo("createdAt", Timestamp(endOfMonth.time))

        budgetRef.get().addOnSuccessListener { document ->
            for (doc in document) {
                budget = doc.data["amount"].toString().toInt()
                break
            }
        }

        expenseRef.get().addOnSuccessListener { document ->
            for (doc in document) {
                expense += doc.data["amount"].toString().toInt()
            }
        }

        val rate = (((expense.toDouble()) / (budget.toDouble())) * 100).toInt()
        return rate
    } catch (e: Exception) {
        Log.d("Home / ExpenseRate", "Expense rate error")
        return 0
    }
}

// 최근 5개 소비 가져옴. 단, 이전 달의 소비 기록이어도 가져오게 되어 있음.
fun getExpenseListForHome(): MutableList<ExpenseDto> {
    val uid = getFirebaseAuth()
    val expenses = mutableListOf<ExpenseDto>()
    try {
        val ref = getFirebaseRef().document(uid!!).collection("expenses")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    val data = doc.data
                    // 디스플레이용. 필수 정보만 넣고 나머지는 안 가져옴
                    val expense = ExpenseDto(
                        amount = data["amount"].toString().toInt(),
                        title = data["title"].toString(),
                        date = data["date"] as Timestamp,
                        categoryId = data["categoryId"].toString(),
                        createdAt = data["createdAt"] as Timestamp
                    )
                    expenses.add(expense)
                }
            }
    } catch (e: Exception) {
        Log.d("Home / RecentExpenses", "Expense list error")
    }
    return expenses
}