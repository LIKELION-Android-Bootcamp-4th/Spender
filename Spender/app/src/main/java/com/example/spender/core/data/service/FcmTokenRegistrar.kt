package com.example.spender.core.data.service

import android.app.Application
import android.util.Log
import com.example.spender.core.data.local.FcmTokenStore
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging

object FcmTokenRegistrar {
    private fun upsert(uid: String, token: String){
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .set(mapOf("fcmToken" to token), SetOptions.merge())
            .addOnSuccessListener { Log.d("FCM", "토큰 업서트 성공") }
            .addOnFailureListener { e -> Log.e("FCM", "토큰 업서트 실패", e) }
    }

    fun handleAfterLogin(app: Application) {
        val uid = Firebase.auth.currentUser?.uid ?: return

        // 1) pending 토큰 처리
        FcmTokenStore.get(app)?.let { pending ->
            upsert(uid, pending)
            FcmTokenStore.clear(app)
        }

        // 2) 현재 토큰 재확보하여 업서트
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token -> upsert(uid, token) }
            .addOnFailureListener { e -> Log.e("FCM", "현재 토큰 조회 실패", e) }
    }
}