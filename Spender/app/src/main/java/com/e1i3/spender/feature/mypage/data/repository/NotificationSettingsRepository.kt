package com.e1i3.spender.feature.mypage.data.repository

import com.e1i3.spender.core.data.remote.notification.NotificationSettingsDto
import com.e1i3.spender.core.data.remote.notification.NotificationSettingsField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationSettingsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    // 기존 알림 설정 가져오기
    suspend fun getNotificationSettings() = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val documentSnapshot = firestore.collection("users")
            .document(uid)
            .get()
            .await()

        val map = documentSnapshot.get("notificationSettings") as? Map<*, *>
        NotificationSettingsDto(
            budgetAlert = map?.get("budgetAlert") as? Boolean ?: false,
            reportAlert = map?.get("reportAlert") as? Boolean ?: false,
            reminderAlert = map?.get("reminderAlert") as? Boolean ?: false,
            reportDeadlineAlert = map?.get("reportDeadlineAlert") as? Boolean ?: false,
        )
    }

    // 알림 설정 업데이트
    suspend fun updateNotificationSetting(
        field: NotificationSettingsField,
        enabled: Boolean,
    ) = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        firestore.collection("users")
            .document(uid)
            .update(mapOf(field.path to enabled))
            .await()
    }

    // db에 notification settings 가 없을 경우
    suspend fun ensureDefaults(
        enabled: Boolean,
    ) = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")
        val userDocRef = firestore.collection("users").document(uid)

        val documentSnapshot = userDocRef.get().await()
        val alreadyHasMap = (documentSnapshot.get("notificationSettings") as? Map<*, *>) != null

        if (!alreadyHasMap) {
            val settingsMap = mapOf(
                "notificationSettings" to mapOf(
                    "budgetAlert" to enabled,
                    "reportAlert" to enabled,
                    "reminderAlert" to enabled,
                    "reportDeadline" to enabled
                )
            )
            userDocRef.set(settingsMap, SetOptions.merge()).await()
        }
    }

}