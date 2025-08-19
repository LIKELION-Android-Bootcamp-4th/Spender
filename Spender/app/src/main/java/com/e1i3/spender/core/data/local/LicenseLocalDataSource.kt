package com.e1i3.spender.core.data.local

import android.content.Context
import com.e1i3.spender.feature.mypage.domain.model.OpenSourceLicense
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LicenseLocalDataSource {
    fun loadLicenses(context: Context): List<OpenSourceLicense> {
        val json = context.assets.open("licenses.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<OpenSourceLicense>>() {}.type
        return Gson().fromJson(json, type)
    }
}
