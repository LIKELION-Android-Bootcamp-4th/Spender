package com.example.spender.feature.auth.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spender.core.data.remote.auth.LoginType
import com.example.spender.core.data.service.FcmTokenRegistrar
import com.example.spender.feature.auth.data.AuthPrefs
import com.example.spender.feature.auth.domain.AuthRepository
import com.example.spender.feature.onboarding.data.OnboardingPref
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository
) : AndroidViewModel(application) {
    
    private val _isFailState = mutableStateOf(false)
    val isFailState: State<Boolean> = _isFailState

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun googleLogin(
        context: Context,
        activityResult: ActivityResult,
        onSuccess: () -> Unit
    ) {
        try {
            _isLoading.value = true
            val account = GoogleSignIn
                .getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AuthPrefs.setLoginType(context, LoginType.GOOGLE)

                        val app = context.applicationContext as android.app.Application
                        FcmTokenRegistrar.handleAfterLogin(app)

                        onSuccess()
                        _isLoading.value = false
                    } else {
                        _isFailState.value = true
                        Log.d("Login", "Firebase SignIn failed! ${task.exception} ")
                        _isLoading.value = false
                    }
                }
        } catch (e: Exception) {
            _isFailState.value = true
            Log.d("Login", "Google SignIn failed! ${e.message}")
            _isLoading.value = false
        }
    }

    fun naverLogin(context: Context, navController: NavController) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                authRepository.naverLogin(context)
                AuthPrefs.setLoginType(getApplication(), LoginType.NAVER)
                Log.d("Login", "Naver Login Success!")

                val currentUser = Firebase.auth.currentUser
                val userMap: Map<String, Any?>? = currentUser?.let {
                    mapOf(
                        "uid" to it.uid,
                        "email" to it.email,
                        "name" to it.displayName,
                        "photoUrl" to it.photoUrl?.toString()
                    )
                }
                Log.d("Login", "naver User : $userMap")

                if (currentUser != null) {
                    val budgetsExist = checkBudgetsCollectionExists(currentUser.uid)
                    navController.navigate(
                        if (budgetsExist) "main" else "onboarding"
                    ) {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                Log.e("Login", "Naver Login Fail", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun kakaoLogin(context: Context, navController: NavController) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                authRepository.kakaoLogin(context)
                AuthPrefs.setLoginType(getApplication(), LoginType.KAKAO)
                Log.d("Login", "Kakao Login Success!")
                val currentUser = Firebase.auth.currentUser
                val userMap: Map<String, Any?>? = currentUser?.let {
                    mapOf(
                        "uid" to it.uid,
                        "email" to it.email,
                        "name" to it.displayName,
                        "photoUrl" to it.photoUrl?.toString()
                    )
                }
                Log.d("Login", "kakao User : ${userMap.toString()}")

                if (currentUser != null) {
                    val budgetsExist = checkBudgetsCollectionExists(currentUser.uid)
                    navController.navigate(
                        if (budgetsExist) "main" else "onboarding"
                    ) {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                Log.e("Login", "Naver Login Fail", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout(context: Context, onDone: () -> Unit) {
        viewModelScope.launch {
            authRepository.logoutUser(context)
            onDone()
        }
    }

    fun withdraw(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.withdrawUser(context)
                onSuccess()
            } catch (e: Exception) {
                Log.e("Withdraw", "Unlink Failed", e)
                onError(e.message ?: "error")
            }
        }
    }

    // budgets 컬렉션이 있는지 확인
    suspend fun checkBudgetsCollectionExists(uid: String): Boolean {
        return try {
            val snapshot = Firebase.firestore
                .collection("users")
                .document(uid)
                .collection("budgets")
                .limit(1)
                .get()
                .await()

            !snapshot.isEmpty
        } catch (e: Exception) {
            Log.e("Firestore", "Error checking budgets collection", e)
            false
        }
    }

}