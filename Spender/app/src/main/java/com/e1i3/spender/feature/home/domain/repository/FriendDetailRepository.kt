package com.e1i3.spender.feature.home.domain.repository


import com.e1i3.spender.core.data.remote.friend.FriendDetailDto
import com.e1i3.spender.core.data.remote.report.CategoryTotalDto
import com.e1i3.spender.core.data.remote.report.EmotionTotalDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FriendDetailRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getFriendDetail(friendId: String) = runCatching {
        val monthStr = getCurrentMonthString()
        val (start, end) = getMonthRange(monthStr)

        val userRef = firestore.collection("users").document(friendId)

        val userSnapshot = userRef.get().await()
        val nickname = userSnapshot.getString("nickname") ?: ""

        val expensesSnapshot = userRef.collection("expenses")
            .whereGreaterThanOrEqualTo("date", start)
            .whereLessThan("date", end)
            .get()
            .await()

        val budgetSnapshot = userRef.collection("budgets")
            .document(monthStr)
            .get()
            .await()

        val categoriesSnapshot = userRef.collection("categories")
            .get()
            .await()

        val totalBudget = (budgetSnapshot.get("amount") as? Number)?.toInt() ?: 0

        val categoryMeta = categoriesSnapshot.documents.associate {
            it.id to mapOf(
                "name" to (it.data?.get("name") as? String ?: "기타"),
                "color" to (it.data?.get("color") as? String ?: "#CCCCCC")
            )
        }

        var totalExpense = 0
        val categoryMap = mutableMapOf<String, CategoryTotalDto>()
        val emotionMap = mutableMapOf<String, Int>()

        for (doc in expensesSnapshot) {
            val amount = (doc.get("amount") as? Number)?.toInt() ?: continue
            val categoryId = doc.get("categoryId") as? String ?: "unknown"
            val emotionId = doc.get("emotionId") as? String

            totalExpense += amount

            val meta = categoryMeta[categoryId] ?: mapOf("name" to "기타", "color" to "#CCCCCC")
            val (name, color) = meta["name"]!! to meta["color"]!!

            categoryMap[categoryId] = categoryMap[categoryId]?.let {
                it.copy(totalPrice = it.totalPrice + amount)
            } ?: CategoryTotalDto(categoryId, name, amount, color)

            if (emotionId != null) {
                emotionMap[emotionId] = (emotionMap[emotionId] ?: 0) + 1
            }
        }

        FriendDetailDto(
            nickname = nickname,
            totalBudget = totalBudget,
            totalExpense = totalExpense,
            byCategory = categoryMap.values.toList(),
            byEmotion = emotionMap.map { (id, count) ->
                EmotionTotalDto(emotionId = id, amount = count)
            }
        )
    }

    fun getCurrentMonthString(): String {
        val formatter = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun getMonthRange(monthStr: String): Pair<Date, Date> {
        val formatter = SimpleDateFormat("yyyy-MM", Locale.getDefault())

        // 이번달 1일
        val calendar = Calendar.getInstance()
        val startDate = formatter.parse(monthStr) ?: Date()
        calendar.time = startDate
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.time

        // 오늘
        val endCalendar = Calendar.getInstance()
        endCalendar.set(Calendar.HOUR_OF_DAY, 23)
        endCalendar.set(Calendar.MINUTE, 59)
        endCalendar.set(Calendar.SECOND, 59)
        endCalendar.set(Calendar.MILLISECOND, 999)
        val end = endCalendar.time

        return Pair(start, end)
    }

}