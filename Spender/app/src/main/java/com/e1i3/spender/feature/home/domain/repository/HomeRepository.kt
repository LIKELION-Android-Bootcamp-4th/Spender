package com.e1i3.spender.feature.home.domain.repository

import com.e1i3.spender.core.data.remote.friend.FriendListDto
import com.e1i3.spender.feature.home.mapper.toDomain
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
}