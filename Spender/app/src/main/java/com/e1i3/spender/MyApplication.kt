package com.e1i3.spender

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)

        NaverIdLoginSDK.initialize(
            context = this,
            clientId = BuildConfig.NAVER_CLIENT_ID,
            clientSecret = BuildConfig.NAVER_CLIENT_SECRET,
            clientName = getString(R.string.app_name)
        )
        setupShortcuts(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = "push_general_high" // 서버 channelId와 동일
            val nm = getSystemService(NotificationManager::class.java)
            val existing = nm.getNotificationChannel(id)

            if (existing == null || existing.importance != NotificationManager.IMPORTANCE_HIGH) {
                if (existing != null) {
                    nm.deleteNotificationChannel(id)
                }
                val name = "기본 알림"
                val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "Spender 일반 알림 채널"
                    setShowBadge(true)
                    enableVibration(true)
                    enableLights(true)
                    lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                }
                nm.createNotificationChannel(channel)
            }
        }

    }

    private fun setupShortcuts(context: Context) {
        val expenseShortcut = ShortcutInfoCompat.Builder(context, "shortcut_expense_add_dynamic")
            .setShortLabel(context.getString(R.string.shortcut_expense_short))
            .setLongLabel(context.getString(R.string.shortcut_expense_long))
            .setIcon(IconCompat.createWithResource(context, R.drawable.spender_happy))
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    "spender://expense_registration".toUri()
                ).setPackage(context.packageName) // 우리 앱으로만 열리도록
            )
            .build()

        ShortcutManagerCompat.setDynamicShortcuts(context, listOf(expenseShortcut))
    }
}