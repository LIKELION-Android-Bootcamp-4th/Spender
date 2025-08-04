package com.example.spender.core.data.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

fun getFirebaseAuth(): String? = FirebaseAuth.getInstance().currentUser?.uid

fun login(user: FirebaseUser) {
    val ref = FirebaseFirestore.getInstance().collection("users").document(user.uid)
    ref.get().addOnSuccessListener { document ->
        if (!document.exists()) {
            val userInfo = mapOf(
                "email" to user.email,
                "displayName" to user.displayName,
                "createdAt" to FieldValue.serverTimestamp()
            )
            ref.set(userInfo)
        }
    }
}

fun logout() {
    FirebaseAuth.getInstance().signOut()
}