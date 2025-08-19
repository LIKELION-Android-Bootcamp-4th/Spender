package com.e1i3.spender.feature.auth.domain

import android.content.Context
import com.e1i3.spender.core.data.remote.auth.LoginType
import com.e1i3.spender.feature.auth.data.AuthPrefs
import com.e1i3.spender.feature.auth.data.FirebaseAuthDataSource
import com.e1i3.spender.feature.auth.data.KakaoDataSource
import com.e1i3.spender.feature.auth.data.NaverDataSource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
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
        signInWithCustomToken("naverCustomAuth", accessToken)
    }

    suspend fun kakaoLogin(context: Context) {
        val accessToken = kakaoDataSource.signIn(context)
        signInWithCustomToken("kakaoCustomAuth", accessToken)
    }

    private suspend fun signInWithCustomToken(functionName: String, accessToken: String) {
        val result = firebaseAuthDataSource.requestCustomToken(functionName, accessToken).await()
        val customToken = (result.data as Map<*, *>)["token"] as String

        try {
            val authResult = firebaseAuthDataSource.signInWithCustomToken(customToken).await()
        } catch (e: Exception) {
            throw e
        }
    }

    fun logoutUser(context: Context) {
        when (AuthPrefs.getLoginType(context)) {
            LoginType.KAKAO -> {
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                    } else {
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
                ).signOut().addOnCompleteListener {
                }
            }

            else -> {
            }
        }

        FirebaseAuth.getInstance().signOut()

        AuthPrefs.clear(context)
    }

    suspend fun withdrawUser(context: Context) {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid ?: throw Exception("No authenticated user")

        deleteAllUserData(uid)

        try {
            user.delete().await()
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            throw Exception("재인증이 필요합니다.")
        }

        when (AuthPrefs.getLoginType(context)) {
            LoginType.KAKAO -> {
                UserApiClient.instance.unlink { error ->
                    if (error != null) {
                    } else {
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

    private suspend fun deleteAllUserData(uid: String) {
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("users").document(uid)

        try {
            val subcollections = listOf(
                "categories",
                "expenses",
                "incomes",
                "regular_expenses",
                "reports",
                "budgets",
                "notifications"
            )

            subcollections.forEach { collectionName ->
                deleteSubcollection(userDocRef.collection(collectionName))
            }
            userDocRef.delete().await()
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun deleteSubcollection(collectionRef: CollectionReference) {
        try {
            val documents = collectionRef.get().await()

            val batches = documents.documents.chunked(500)

            batches.forEach { batch ->
                val writeBatch = FirebaseFirestore.getInstance().batch()
                batch.forEach { document ->
                    writeBatch.delete(document.reference)
                }
                writeBatch.commit().await()
            }
        } catch (e: Exception) {
            throw e
        }
    }

}