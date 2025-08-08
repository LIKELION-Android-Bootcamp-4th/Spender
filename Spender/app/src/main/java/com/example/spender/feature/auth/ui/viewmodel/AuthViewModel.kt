package com.example.spender.feature.auth.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spender.core.data.remote.auth.LoginType
import com.example.spender.feature.auth.data.AuthPrefs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel : ViewModel() {
    private val _isFailState = mutableStateOf(false)
    val isFailState: State<Boolean> = _isFailState

    fun googleLogin(
        context: Context,
        activityResult: ActivityResult,
        onSuccess: () -> Unit
    ) {
        try {
            val account = GoogleSignIn
                .getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AuthPrefs.setLoginType(context, LoginType.GOOGLE)
                        onSuccess()
                    } else {
                        _isFailState.value = true
                        Log.d("Login", "Firebase SignIn failed! ${task.exception} ")
                    }
                }
        } catch (e: Exception) {
            _isFailState.value = true
            Log.d("Login", "Google SignIn failed! ${e.message}")
        }
    }
}