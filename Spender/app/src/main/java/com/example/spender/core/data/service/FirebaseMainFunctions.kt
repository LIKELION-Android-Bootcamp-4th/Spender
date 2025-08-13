package com.example.spender.core.data.service

import android.util.Log
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Calendar

// 총 지출 금액을 가져옴.
suspend fun getTotalExpense(): Int {
    val uid = getFirebaseAuth()
    return try {
        val ref = getFirebaseRef().document(uid!!).collection("expenses")
        val document = ref.get().await()

        var total = 0
        for (doc in document.documents) {
            total += doc.data?.get("amount")?.toString()?.toInt() ?: 0
        }
        Log.d("Home", "TotalBudget: $total")
        total
    } catch (e: Exception) {
        Log.e("Home / TotalBudget", "total budget error: ${e.message}")
        0
    }
}

// 예산 소진 비율'만' 가져옴. 비율이 위험한지 아닌지는 따로 비즈니스 로직 필요
suspend fun getExpenseRate(): Float {
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

    return try {
        val budgetRef = getFirebaseRef().document(uid!!).collection("budgets")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
        val expenseRef = getFirebaseRef().document(uid).collection("expenses")
            .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfMonth.time))
            .whereLessThanOrEqualTo("createdAt", Timestamp(endOfMonth.time))

        val budgetDocument = budgetRef.get().await()
        val expenseDocument = expenseRef.get().await()

        var budget = 1
        var expense = 0

        for (doc in budgetDocument) {
            budget = doc.data["amount"].toString().toInt()
            break
        }

        for (doc in expenseDocument) {
            expense += doc.data["amount"].toString().toInt()
        }

        Log.d("Home", "getExpenseRate expense: $expense")
        Log.d("Home", "getExpenseRate budget: $budget")

        val rate = if (budget > 0) {
            (expense.toDouble() / budget.toDouble() * 100).toFloat()
        } else {
            0f
        }

        Log.d("Home", "getExpenseRate: $rate")
        rate
    } catch (e: Exception) {
        Log.e("Home / ExpenseRate", "Expense rate error: ${e.message}")
        0f
    }
}

// 최근 5개 소비 가져옴. 단, 이전 달의 소비 기록이어도 가져오게 되어 있음.
suspend fun getExpenseListForHome(): List<ExpenseDto> {
    val uid = getFirebaseAuth()
    return try {
        val document = getFirebaseRef().document(uid!!).collection("expenses")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .await()

        val expenses = mutableListOf<ExpenseDto>()
        for (doc in document) {
            val data = doc.data
            val expense = ExpenseDto(
                id = doc.id,
                amount = data["amount"].toString().toInt(),
                title = data["title"].toString(),
                date = data["date"] as Timestamp,
                categoryId = data["categoryId"].toString(),
                createdAt = data["createdAt"] as Timestamp
            )
            expenses.add(expense)
        }
        expenses
    } catch (e: Exception) {
        Log.e("Home / RecentExpenses", "Expense list error: ${e.message}")
        emptyList()
    }
}