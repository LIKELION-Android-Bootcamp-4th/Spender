package com.example.spender.feature.mypage.data.repository

import android.util.Log
import com.example.spender.core.data.remote.notification.NotificationSettingsDto
import com.example.spender.core.data.remote.notification.NotificationSettingsField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class NotificationSettingsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private fun uidOrNull(): String? = auth.currentUser?.uid

    // 기존 알림 설정 가져오기
    fun getNotificationSettings(
        onSuccess: (NotificationSettingsDto) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = uidOrNull()
        if (uid == null) {
            onError(IllegalStateException("로그아웃 상태"))
            return
        }

        firestore.collection("users").document(uid)
            .get().addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    onError(IllegalStateException("user 문서 없음"))
                    return@addOnSuccessListener
                }

                val map = doc.get("notificationSettings") as? Map<*, *>
                val dto = NotificationSettingsDto(
                    budgetAlert = map?.get("budgetAlert") as? Boolean ?: false,
                    reportAlert = map?.get("reportAlert") as? Boolean ?: false,
                    reminderAlert = map?.get("reminderAlert") as? Boolean ?: false
                )
                onSuccess(dto)
            }
            .addOnFailureListener { onError(it) }
    }

    // 알림 설정 업데이트
    fun updateNotificationSetting(
        field: NotificationSettingsField,
        enabled: Boolean,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = uidOrNull()
        if (uid == null) {
            onError(IllegalStateException("로그아웃 상태"))
            return
        }

        firestore.collection("users").document(uid)
            .update(mapOf(field.path to enabled))
            .addOnSuccessListener {
                Log.d("알림 레포지토리", "업데이트 -> ${field.name}=$enabled")
                onSuccess()
            }
            .addOnFailureListener{ onError(it) }

    }

    // db에 notification settings 가 없을 경우
    fun ensureDefaults(
        enabled: Boolean,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = uidOrNull() ?: return onError(IllegalStateException("User not logged in"))

        val userDoc = firestore.collection("users").document(uid)
        userDoc.get()
            .addOnSuccessListener { snap ->
                val exists = snap.exists()
                val alreadyHasMap = (snap.get("notificationSettings") as? Map<*, *>) != null

                if (exists && alreadyHasMap) {
                    // 이미 설정 있음 → 아무것도 안 함
                    onSuccess()
                    return@addOnSuccessListener
                }

                val settingsMap = mapOf(
                    "notificationSettings" to mapOf(
                        "budgetAlert" to enabled,
                        "reportAlert" to enabled,
                        "reminderAlert" to enabled
                    )
                )

                userDoc.set(settingsMap, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d("알림 레포지토리", "기본 설정 생성됨 -> enabled=$enabled")
                        onSuccess()
                    }
                    .addOnFailureListener { onError(it) }
            }
            .addOnFailureListener { onError(it) }
    }

}