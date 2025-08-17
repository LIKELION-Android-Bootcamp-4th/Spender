package com.example.spender.feature.home.domain.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class HomeRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
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

    fun observeUnreadNotifications(onChange: (Boolean)-> Unit): ListenerRegistration?{
        val uid = auth.currentUser?.uid ?: return null

        return firestore.collection("users")
            .document(uid)
            .collection("notifications")
            .whereEqualTo("isRead", false)
            .addSnapshotListener{ snapshot, error ->
                if(error != null){
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                val hasUnread = snapshot?.isEmpty == false
                onChange(hasUnread)
            }
    }
}