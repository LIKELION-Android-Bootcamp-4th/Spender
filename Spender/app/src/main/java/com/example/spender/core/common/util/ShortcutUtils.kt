package com.example.spender.core.common.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.example.spender.R

private const val SHORTCUT_ID_EXPENSE_ADD = "shortcut_expense_add"

fun requestPinExpenseShortcut(context: Context) {
    if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
        Toast.makeText(context, "이 런처는 고정 단축키를 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
        return
    }

    val deepLink = Intent(Intent.ACTION_VIEW, "spender://expense_registration".toUri())
        .setPackage(context.packageName)

    val shortcut = ShortcutInfoCompat.Builder(context, SHORTCUT_ID_EXPENSE_ADD)
        .setShortLabel(context.getString(R.string.spender_mini_widget_label))
        .setLongLabel(context.getString(R.string.spender_mini_widget_label))
        .setIcon(IconCompat.createWithResource(context, R.drawable.spender_happy))
        .setIntent(deepLink)
        .build()

    ShortcutManagerCompat.requestPinShortcut(context, shortcut, null)
}
