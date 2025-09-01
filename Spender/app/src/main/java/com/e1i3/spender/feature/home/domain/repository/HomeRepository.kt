package com.e1i3.spender.feature.home.domain.repository

import com.e1i3.spender.core.data.remote.expense.ExpenseDto
import com.e1i3.spender.feature.home.domain.model.Friend
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
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

    fun observeFriends(): Flow<List<Friend>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            close(IllegalStateException("로그아웃 상태"))
            return@callbackFlow
        }

        val listener = firestore.collection("users")
            .document(uid)
            .collection("friends")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val friends: List<Friend> = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Friend::class.java)?.copy(userId = doc.id)
                } ?: emptyList()

                trySend(friends)
            }

        awaitClose { listener.remove() }
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

    fun observeCurrentTier(): Flow<Int> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            close(IllegalStateException("로그아웃 상태"))
            return@callbackFlow
        }

        val listener = firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val tier = snapshot?.getLong("currentTier")?.toInt() ?: 3
                trySend(tier)
            }

        awaitClose { listener.remove() }
    }

    fun observeTotalExpense(): Flow<Int> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            close(IllegalStateException("로그아웃 상태"))
            return@callbackFlow
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = Timestamp(calendar.time)

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val endOfMonth = Timestamp(calendar.time)

        val listener = firestore.collection("users")
            .document(uid)
            .collection("expenses")
            .whereGreaterThanOrEqualTo("date", startOfMonth)
            .whereLessThanOrEqualTo("date", endOfMonth)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                var total = 0
                snapshot?.documents?.forEach { doc ->
                    total += doc.data?.get("amount")?.toString()?.toInt() ?: 0
                }
                trySend(total)
            }
        awaitClose { listener.remove() }
    }

    fun observeExpenseRate(): Flow<Float> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            close(IllegalStateException("로그아웃 상태"))
            return@callbackFlow
        }

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

        suspend fun calculateAndSendRate() {
            try {
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

                trySend(rate)
            } catch (e: Exception) {
            }
        }

        val budgetListener = firestore.collection("users")
            .document(uid)
            .collection("budgets")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { _, _ ->
                launch { calculateAndSendRate() }
            }

        val expenseListener = firestore.collection("users")
            .document(uid)
            .collection("expenses")
            .whereGreaterThanOrEqualTo("createdAt", Timestamp(startOfMonth.time))
            .whereLessThanOrEqualTo("createdAt", Timestamp(endOfMonth.time))
            .addSnapshotListener { _, _ ->
                launch { calculateAndSendRate() }
            }

        launch { calculateAndSendRate() }

        awaitClose {
            budgetListener.remove()
            expenseListener.remove()
        }
    }

    fun observeRecentExpenses(): Flow<List<ExpenseDto>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            close(IllegalStateException("로그아웃 상태"))
            return@callbackFlow
        }

        val listener = firestore.collection("users")
            .document(uid)
            .collection("expenses")
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(5)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val expenses = snapshot?.documents?.map { doc ->
                    val data = doc.data ?: return@map null

                    val amount = try {
                        data["amount"].toString().toInt()
                    } catch (_: Exception) {
                        0
                    }

                    val title = data["title"].toString()

                    val dateTs: Timestamp? = doc.getTimestamp("date")
                        ?: (data["date"] as? Timestamp)

                    val createdAtTs: Timestamp? = doc.getTimestamp("createdAt")
                        ?: (data["createdAt"] as? Timestamp)

                    val categoryId = data["categoryId"].toString()
                    val emotionId = (doc.get("emotion") as? String)
                        ?: data["emotion"]?.toString()
                        ?: data["emotionId"]?.toString()
                        ?: ""

                    if (dateTs == null || createdAtTs == null) {
                        return@map null
                    }

                    ExpenseDto(
                        id = doc.id,
                        amount = amount,
                        title = title,
                        date = dateTs,
                        emotionId = emotionId,
                        categoryId = categoryId,
                        createdAt = createdAtTs
                    )
                }?.filterNotNull() ?: emptyList()
                trySend(expenses)
            }
        awaitClose { listener.remove() }
    }
}