package com.e1i3.spender.core.data.service

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Calendar

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

        val rate = if (budget > 0) {
            (expense.toDouble() / budget.toDouble() * 100).toFloat()
        } else {
            0f
        }

        rate
    } catch (e: Exception) {
        0f
    }
}
