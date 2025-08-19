package com.e1i3.spender.feature.home.domain.repository

import com.e1i3.spender.core.data.remote.notification.NotificationDto
import com.e1i3.spender.feature.home.mapper.toDomain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class NotificationListRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getNotificationList(
    ) = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val querySnapshot = firestore.collection("users")
            .document(uid)
            .collection("notifications")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()

        val items = querySnapshot.documents.mapNotNull { docSnap ->
            docSnap.toObject(NotificationDto::class.java)?.toDomain(docSnap.id)
        }
        items
    }

    suspend fun markAllAsRead(): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("notifications")
            .whereEqualTo("isRead", false)
            .get()
            .await()

        snapshot.documents.forEach { doc ->
            doc.reference.update("isRead", true).await()
        }
    }

}