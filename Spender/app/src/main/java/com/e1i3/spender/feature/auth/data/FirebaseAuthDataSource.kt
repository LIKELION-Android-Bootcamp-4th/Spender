package com.e1i3.spender.feature.auth.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.functions.functions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource @Inject constructor() {
    private val auth = Firebase.auth
    private val functions = Firebase.functions

    fun signInWithCustomToken(token: String) =
        auth.signInWithCustomToken(token)

    fun requestCustomToken(functionName: String, accessToken: String) =
        functions.getHttpsCallable(functionName)
            .call(mapOf("accessToken" to accessToken))
}