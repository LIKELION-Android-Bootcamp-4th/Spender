package com.example.spender.feature.auth.domain

import android.content.Context
import android.util.Log
import com.example.spender.core.data.remote.auth.LoginType
import com.example.spender.core.data.service.getFirebaseRef
import com.example.spender.feature.auth.data.AuthPrefs
import com.example.spender.feature.auth.data.FirebaseAuthDataSource
import com.example.spender.feature.auth.data.NaverDataSource
import com.example.spender.feature.auth.data.KakaoDataSource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val naverDataSource: NaverDataSource,
    private val kakaoDataSource: KakaoDataSource
) {

    suspend fun naverLogin(context: Context) {
        val accessToken = naverDataSource.signIn(context)
        Log.d("Login", "naver token : $accessToken")
        signInWithCustomToken("naverCustomAuth", accessToken)
    }

    suspend fun kakaoLogin(context: Context) {
        val accessToken = kakaoDataSource.signIn(context)
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

    fun logoutUser(context: Context) {
        when (AuthPrefs.getLoginType(context)) {
            LoginType.KAKAO -> {
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.e("Logout", "Kakao Logout failed", error)
                    } else {
                        Log.d("Logout", "Kakao Logout success!")
                    }
                }
            }

            LoginType.NAVER -> {
                NaverIdLoginSDK.logout()
                Log.d("Logout", "Naver Logout success!")
            }

            LoginType.GOOGLE -> {
                GoogleSignIn.getClient(
                    context,
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut().addOnCompleteListener {
                    Log.d("Logout", "Google Logout success!")
                }
            }

            else -> {
                Log.d("Logout", "No login type found")
            }
        }

        FirebaseAuth.getInstance().signOut()

        AuthPrefs.clear(context)
    }

    suspend fun withdrawUser(context: Context) {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid ?: throw Exception("No authenticated user")

        getFirebaseRef().document(uid).delete().await()

        try {
            user.delete().await()
            Log.d("Withdraw", "Firebase user deleted")
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            throw Exception("재인증이 필요합니다.")
        }

        when (AuthPrefs.getLoginType(context)) {
            LoginType.KAKAO -> {
                UserApiClient.instance.unlink { error ->
                    if (error != null) {
                        Log.e("Withdraw", "Kakao Unlink Failed", error)
                    } else {
                        Log.d("Withdraw", "Kakao Unlink Success!")
                    }
                }
            }

            LoginType.NAVER -> {
                NaverIdLoginSDK.logout()
            }

            LoginType.GOOGLE -> {
                GoogleSignIn.getClient(
                    context,
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut()
            }

            else -> {}
        }
        AuthPrefs.clear(context)
    }

}