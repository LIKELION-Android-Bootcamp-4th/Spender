package com.example.spender.feature.auth.data

import android.content.Context
import com.example.spender.core.data.remote.auth.LoginType

object AuthPrefs {
    private const val PREF_NAME = "auth_pref"
    private const val LOGIN_TYPE_KEY = "login_type"

    fun setLoginType(context: Context, type: LoginType) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(LOGIN_TYPE_KEY, type.type).apply()
    }

    fun getLoginType(context: Context): LoginType? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val typeStr = prefs.getString(LOGIN_TYPE_KEY, null)
        return typeStr?.let { type -> LoginType.values().find { it.type == type } }
    }

    fun clear(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
