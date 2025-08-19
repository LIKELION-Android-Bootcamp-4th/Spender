package com.e1i3.spender.core.data.local

import android.content.Context
import androidx.core.content.edit

object FcmTokenStore {
    private const val PREF = "fcm_pref"
    private const val KEY = "pendingToken"

    fun save(context: Context, token: String) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit { putString(KEY, token) }
    }

    fun get(context: Context): String? =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getString(KEY, null)

    fun clear(context: Context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit { remove(KEY) }
    }
}