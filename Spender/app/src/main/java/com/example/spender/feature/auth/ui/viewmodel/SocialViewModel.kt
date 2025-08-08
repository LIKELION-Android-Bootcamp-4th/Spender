package com.example.spender.feature.auth.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spender.core.data.remote.auth.LoginType
import com.example.spender.core.data.service.FcmTokenRegistrar
import com.example.spender.feature.auth.data.AuthPrefs
import com.example.spender.feature.auth.domain.AuthRepository
import com.example.spender.feature.onboarding.data.OnboardingPref
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class SocialViewModel(
    application: Application,
    private val authRepository: AuthRepository
) : AndroidViewModel(application) {

    fun naverLogin(navController: NavController) {
        viewModelScope.launch {
            try {
                authRepository.naverLogin()
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

                FcmTokenRegistrar.handleAfterLogin(getApplication())

                Log.d("Login", "naver User : ${userMap.toString()}")

                navController.navigate(
                    if (OnboardingPref.wasShown(getApplication())) "main"
                    else "onboarding") {
                    popUpTo("auth") { inclusive = true }
                }
            } catch (e: Exception) {
                Log.e("Login", "Naver Login Fail", e)
            }
        }
    }

    fun kakaoLogin(navController: NavController) {
        viewModelScope.launch {
            try {
                authRepository.kakaoLogin()
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

                FcmTokenRegistrar.handleAfterLogin(getApplication())

                navController.navigate(
                    if (OnboardingPref.wasShown(getApplication())) "main"
                    else "onboarding") {
                    popUpTo("auth") { inclusive = true }
                }
            } catch (e: Exception) {
                Log.e("Login", "Kakao Login Fail", e)
            }
        }
    }

    fun logout(context: Context, onDone: () -> Unit) {
        viewModelScope.launch {
            authRepository.logoutByLoginType(context)
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


}