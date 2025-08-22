package com.e1i3.spender.feature.home.domain.repository

import com.e1i3.spender.core.data.remote.expense.ExpenseDto
import com.e1i3.spender.core.data.remote.friend.FriendListDto
import com.e1i3.spender.feature.home.mapper.toDomain
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class HomeRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun hasUnreadNotifications(): Result<Boolean> = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("notifications")
            .whereEqualTo("isRead", false)
            .limit(1)
            .get()
            .await()

        !snapshot.isEmpty
    }

    fun observeUnreadNotifications(onChange: (Boolean) -> Unit): ListenerRegistration? {
        val uid = auth.currentUser?.uid ?: return null

        return firestore.collection("users")
            .document(uid)
            .collection("notifications")
            .whereEqualTo("isRead", false)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                val hasUnread = snapshot?.isEmpty == false
                onChange(hasUnread)
            }
    }

    suspend fun getFriendList() = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("friends")
            .get()
            .await()

        snapshot.documents.mapNotNull { doc ->
            val dto = doc.toObject(FriendListDto::class.java)
            dto?.toDomain(userId = doc.id)
        }
    }

    suspend fun deleteFriend(friendId: String) = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        firestore.collection("users")
            .document(uid)
            .collection("friends")
            .document(friendId)
            .delete()
            .await()

        firestore.collection("users")
            .document(friendId)
            .collection("friends")
            .document(uid)
            .delete()
            .await()
    }

    suspend fun getCurrentTier() = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val snap = firestore.collection("users").document(uid).get().await()
        snap.getLong("currentTier")?.toInt() ?: 3
    }

    suspend fun getTotalExpense(): Result<Int> = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val ref = firestore.collection("users").document(uid).collection("expenses")
        val document = ref.get().await()

        var total = 0
        for (doc in document.documents) {
            total += doc.data?.get("amount")?.toString()?.toInt() ?: 0
        }
        total
    }

    suspend fun getExpenseRate(): Result<Float> = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

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

        val budgetRef = firestore.collection("users").document(uid).collection("budgets")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
        val expenseRef = firestore.collection("users").document(uid).collection("expenses")
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
    }

    suspend fun getExpenseListForHome(): Result<List<ExpenseDto>> = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val document = firestore.collection("users").document(uid).collection("expenses")
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
    }
}