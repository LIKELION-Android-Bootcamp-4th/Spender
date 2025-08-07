package com.example.spender.feature.auth.data

import android.content.Context
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class KakaoDataSource(private val context: Context) {
    suspend fun signIn(): String = suspendCancellableCoroutine { cont ->
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                Log.e("Login", "Kakao SignIn Error! ${error.message}", error)
                cont.resumeWithException(error)
            } else if (token != null) {
                cont.resume(token.accessToken, null)
            } else {
                cont.resumeWithException(Exception("Kakao SignIn Failed!: unknown error"))
            }
        }
    }
}