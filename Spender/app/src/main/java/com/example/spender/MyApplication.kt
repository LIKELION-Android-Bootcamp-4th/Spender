package com.example.spender

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
        // 지출 등록으로 바로 가는 단축키 (딥링크: spender://expense_registration)
        val expenseShortcut = ShortcutInfoCompat.Builder(context, "shortcut_expense_add_dynamic")
            .setShortLabel(context.getString(R.string.shortcut_expense_short)) // "지출 등록"
            .setLongLabel(context.getString(R.string.shortcut_expense_long))   // "지출을 바로 등록합니다."
            .setIcon(IconCompat.createWithResource(context, R.drawable.spender_happy))
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    "spender://expense_registration".toUri()
                ).setPackage(context.packageName) // 우리 앱으로만 열리도록
            )
            .build()

        // 정의한 목록으로 교체(덮어쓰기). 필요하면 여러 개 넣어도 됨.
        ShortcutManagerCompat.setDynamicShortcuts(context, listOf(expenseShortcut))

        // (옵션) 이후 상황에 따라 갱신하고 싶을 땐 setDynamicShortcuts/disableShortcuts 등을 적절히 재호출
        // 주의: OS 쿼터(보통 24h 내 10회) 있으니 잦은 업데이트는 피하기
    }
}