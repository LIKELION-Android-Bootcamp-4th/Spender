package com.example.spender.feature.auth.domain

import android.content.Intent
import android.util.Log
import com.example.spender.feature.auth.data.FirebaseAuthDataSource
import com.example.spender.feature.auth.data.NaverDataSource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val naverDataSource: NaverDataSource
) {

    suspend fun naverLogin() {
        val accessToken = naverDataSource.signIn()
        Log.d("AuthRepository", "naver token : $accessToken")
        signInWithCustomToken("naverCustomAuth", accessToken)
    }

    suspend fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.result
        val idToken = account.idToken ?: throw Exception("idToken is null")
        firebaseAuthDataSource.signInWithGoogleCredential(idToken).await()
    }

    private suspend fun signInWithCustomToken(functionName: String, accessToken: String) {
        val result = firebaseAuthDataSource.requestCustomToken(functionName, accessToken).await()
        val customToken = (result.data as Map<*, *>)["token"] as String

        Log.d("AuthRepository", "signInWithCustomToken token : $customToken")
        try {
            val authResult = firebaseAuthDataSource.signInWithCustomToken(customToken).await()
            Log.d("AuthRepository", "signInWithCustomToken success: ${authResult.user?.uid}")
        } catch (e: Exception) {
            Log.e("AuthRepository", "signInWithCustomToken failed", e)
            throw e
        }
    }
}