package com.e1i3.spender.feature.auth.data

import android.content.Context
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@Singleton
class KakaoDataSource @Inject constructor() {
    suspend fun signIn(context: Context): String = suspendCancellableCoroutine { cont ->
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                cont.resumeWithException(error)
            } else if (token != null) {
                cont.resume(token.accessToken, null)
            } else {
                cont.resumeWithException(Exception("Kakao SignIn Failed!: unknown error"))
            }
        }
    }
}