package com.example.spender.core.data.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getFirebaseAuth(): String? = FirebaseAuth.getInstance().currentUser?.uid

fun getFirebaseRef(): CollectionReference = FirebaseFirestore.getInstance().collection("users")

fun login(user: FirebaseUser, provider: String) {
    val ref = FirebaseFirestore.getInstance().collection("users").document(user.uid)
    ref.get().addOnSuccessListener { document ->
        if (!document.exists()) {
            val userInfo = mapOf(
                "email" to user.email,
                "provider" to provider
            )
            ref.set(userInfo)
        }
    }
}

fun setUserBudget(budget: Int) {
    val uid = getFirebaseAuth()
    val timeStamp = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date(System.currentTimeMillis()))
    try {
        val ref = getFirebaseRef().document(uid!!).collection("budgets").document(timeStamp)
        val budgetInfo = mapOf(
            "amount" to budget,
            "month" to timeStamp
        )
    } catch (e: Exception) {
        Log.d("Onboarding / budget", "budget set incomplete - is uid available?")
    }
}

fun logout() {
    FirebaseAuth.getInstance().signOut()
}