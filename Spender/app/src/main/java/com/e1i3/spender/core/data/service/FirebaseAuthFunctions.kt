package com.e1i3.spender.core.data.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

fun getFirebaseAuth(): String? = FirebaseAuth.getInstance().currentUser?.uid

fun getFirebaseRef(): CollectionReference = FirebaseFirestore.getInstance().collection("users")

fun login(user: FirebaseUser?, provider: String) {
    if (user != null) {
        val ref = getFirebaseRef().document(user.uid)
        ref.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val userInfo = mapOf(
                    "email" to user.email,
                    "provider" to provider,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                ref.set(userInfo)
            }
        }
    } else {
    }
}

fun logout() {
    FirebaseAuth.getInstance().signOut()
}