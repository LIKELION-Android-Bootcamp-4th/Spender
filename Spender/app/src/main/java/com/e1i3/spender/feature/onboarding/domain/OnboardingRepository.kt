package com.e1i3.spender.feature.onboarding.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class OnboardingRepository @Inject constructor() {
    private val userCollection = FirebaseFirestore.getInstance().collection("users")
    private val auth = FirebaseAuth.getInstance()
    private fun getUid(): String? = auth.currentUser?.uid

    fun getUserBudget(onResult: (Int?) -> Unit) {
        val uid = getUid() ?: return onResult(null)
        val timeStamp = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())

        userCollection
            .document(uid)
            .collection("budgets")
            .document(timeStamp)
            .get()
            .addOnSuccessListener { document ->
                val amount = document.getLong("amount")?.toInt()
                onResult(amount)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun setUserBudget(budget: Int, onComplete: (Boolean) -> Unit) {
        val uid = getUid() ?: return onComplete(false)
        val timeStamp = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())

        val budgetInfo = mapOf(
            "amount" to budget,
            "createdAt" to FieldValue.serverTimestamp()
        )

        userCollection
            .document(uid)
            .collection("budgets")
            .document(timeStamp)
            .set(budgetInfo)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun setCurrentTier(onComplete: (Boolean) -> Unit) {
        val uid = getUid() ?: return onComplete(false)
        
        val tierInfo = mapOf(
            "currentTier" to 3
        )

        userCollection
            .document(uid)
            .update(tierInfo)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

}