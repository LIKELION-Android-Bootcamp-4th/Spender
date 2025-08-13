package com.example.spender.feature.home.domain.repository

import android.util.Log
import com.example.spender.core.data.remote.notification.NotificationDto
import com.example.spender.feature.home.domain.Notification
import com.example.spender.feature.home.mapper.toDomain
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
            Log.d("NotificationRepo", "docId=${docSnap.id}, raw=${docSnap.data}")
            docSnap.toObject(NotificationDto::class.java)?.toDomain(docSnap.id)
        }
        items
    }
}