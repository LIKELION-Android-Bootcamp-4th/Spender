package com.example.spender.core.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.spender.MainActivity
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

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            FcmTokenStore.save(applicationContext, token)
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .set(mapOf("fcmToken" to token), SetOptions.merge())
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 데이터 페이로드
        val data = remoteMessage.data
        val title = data["title"] ?: remoteMessage.notification?.title ?: "알림"
        val body = data["body"] ?: remoteMessage.notification?.body ?: ""
        val route = data["route"] // "reports" | "home" | "analysis"
        val month = data["month"] // "YYYY-MM" (예: 2025-08)
        val regularExpenseName = data["regularExpenseName"]

        showNotification(title, body, route, month, regularExpenseName)
    }

    private fun showNotification(
        title: String,
        message: String,
        route: String?,
        month: String?,
        regularExpenseName: String?
    ) {
        val channelId = "push_general_high"
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel(channelId, "기본 알림", NotificationManager.IMPORTANCE_HIGH)
            )
        }

        //  알림 탭 시 MainActivity로 이동 + extras 전달
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            route?.let { putExtra("route", it) }
            month?.let { putExtra("month", it) }
            regularExpenseName?.let { putExtra("regularExpenseName", it) }
        }

        val pi = PendingIntent.getActivity(
            this,
            (route ?: "default").hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.spender_happy)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .setTimeoutAfter(20_000)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .build()

        nm.notify(System.currentTimeMillis().toInt(), notification)
    }
}