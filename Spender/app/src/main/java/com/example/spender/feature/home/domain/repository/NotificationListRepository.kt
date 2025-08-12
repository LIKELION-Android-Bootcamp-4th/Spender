package com.example.spender.feature.home.domain.repository

import android.util.Log
import com.example.spender.core.data.remote.notification.NotificationDto
import com.example.spender.feature.home.domain.Notification
import com.example.spender.feature.home.mapper.toDomain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jakarta.inject.Inject

class NotificationListRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun getNotificationList(
        onSuccess: (List<Notification>) -> Unit,
        onError: (Exception) -> Unit
    ){
        val uid = auth.currentUser?.uid
        Log.d("NotificationRepo", "UID 확인용: $uid")
        if (uid == null) {
            Log.d("NotificationRepo", "UID가 널!!!!!")
            onError(IllegalStateException("User not logged in"))
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("notifications")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snap ->
                Log.d("NotificationRepo", "조회 성공! 문서 개수=${snap.size()}")
                val items = snap.documents.mapNotNull { doc ->
                    Log.d("NotificationRepo", "docId=${doc.id}, raw=${doc.data}")
                    doc.toObject(NotificationDto::class.java)?.toDomain(doc.id)
                }
                onSuccess(items)
            }
            .addOnFailureListener { e ->
                Log.e("NotificationRepo", "Firestore getNotificationList 실패: ${e.message}", e)
                onError(e)
            }
    }
}