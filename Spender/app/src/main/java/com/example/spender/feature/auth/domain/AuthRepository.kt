package com.example.spender.feature.auth.domain

import android.util.Log
import com.example.spender.feature.auth.data.FirebaseAuthDataSource
import com.example.spender.feature.auth.data.NaverDataSource
import com.example.spender.feature.auth.data.KakaoDataSource
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val naverDataSource: NaverDataSource,
    private val kakaoDataSource: KakaoDataSource
) {

    suspend fun naverLogin() {
        val accessToken = naverDataSource.signIn()
        Log.d("Login", "naver token : $accessToken")
        signInWithCustomToken("naverCustomAuth", accessToken)
    }

    suspend fun kakaoLogin() {
        val accessToken = kakaoDataSource.signIn()
        Log.d("Login", "kakao token : $accessToken")
        signInWithCustomToken("kakaoCustomAuth", accessToken)
    }

    private suspend fun signInWithCustomToken(functionName: String, accessToken: String) {
        val result = firebaseAuthDataSource.requestCustomToken(functionName, accessToken).await()
        val customToken = (result.data as Map<*, *>)["token"] as String

        Log.d("Login", "signInWithCustomToken token : $customToken")
        try {
            val authResult = firebaseAuthDataSource.signInWithCustomToken(customToken).await()
            Log.d("Login", "signInWithCustomToken success: ${authResult.user?.uid}")
        } catch (e: Exception) {
            Log.e("Login", "signInWithCustomToken failed", e)
            throw e
        }
    }
}