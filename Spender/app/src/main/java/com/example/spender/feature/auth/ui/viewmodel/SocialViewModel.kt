package com.example.spender.feature.auth.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spender.feature.auth.domain.AuthRepository
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
                Log.d("Login", "Naver Login Success")
                val currentUser = Firebase.auth.currentUser
                val userMap: Map<String, Any?>? = currentUser?.let {
                    mapOf(
                        "uid" to it.uid,
                        "email" to it.email,
                        "name" to it.displayName,
                        "photoUrl" to it.photoUrl?.toString()
                    )
                }

                Log.d("Login", "naver User : ${userMap.toString()}")
                navController.navigate("main") {
                    popUpTo("auth") { inclusive = true }
                }
            } catch (e: Exception) {
                Log.e("Login", "Naver Login Fail", e)
            }
        }
    }
}