package com.example.spender.core.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.spender.R
import com.example.spender.core.data.local.FcmTokenStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.HiltAndroidApp

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "새 FCM 토큰: $token")

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            FcmTokenStore.save(applicationContext, token)
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .set(mapOf("fcmToken" to token), SetOptions.merge())
            .addOnSuccessListener { Log.d("FCM", "토큰 Firestore 저장 성공") }
            .addOnFailureListener { e -> Log.e("FCM", "토큰 Firestore 저장 실패", e) }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "메시지 수신됨: ${remoteMessage.data}")

        val title = remoteMessage.notification?.title ?: "알림"
        val body = remoteMessage.notification?.body ?: ""

        showNotification(title, body)
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "default_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "기본 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.spender_happy)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}