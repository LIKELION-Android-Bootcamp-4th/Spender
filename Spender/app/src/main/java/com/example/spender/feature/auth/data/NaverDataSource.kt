package com.example.spender.feature.auth.data

import android.content.Context
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class NaverDataSource(private val context: Context) {

    suspend fun signIn(): String = suspendCancellableCoroutine { cont ->
        NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
            override fun onSuccess() {
                val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                if (naverAccessToken != null) {
                    cont.resume(naverAccessToken, null)
                } else {
                    cont.resumeWithException(Exception("AccessToken is null"))
                }
            }

            override fun onFailure(httpStatus: Int, message: String) {
                cont.resumeWithException(Exception("Login failed: $message"))
            }

            override fun onError(errorCode: Int, message: String) {
                cont.resumeWithException(Exception("Error $errorCode: $message"))
            }
        })
    }
}