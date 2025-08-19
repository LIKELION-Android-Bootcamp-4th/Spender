package com.e1i3.spender

import android.app.Application
import android.content.Context
import android.content.Intent
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