package com.example.spender.feature.onboarding.data

import android.content.Context

object OnboardingPref {
    private const val PREF_NAME = "onboarding_prefs"
    private const val KEY_SHOWN = "onboarding_shown"

    fun setShown(context: Context) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(KEY_SHOWN, true).apply()
    }

    fun wasShown(context: Context): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_SHOWN, false)
    }
}