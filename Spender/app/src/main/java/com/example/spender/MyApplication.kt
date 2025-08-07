package com.example.spender

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, getString(R.string.kakao_app_key))

        NaverIdLoginSDK.initialize(
            context = this,
            clientId = "btOnE17uFYFcVX7H36Tm",
            clientSecret = "lVwABQpwoF",
            clientName = "Spender"
        )

//        var keyHash = Utility.getKeyHash(this)
//        Log.i("GlobalApplication", "$keyHash")
    }
}