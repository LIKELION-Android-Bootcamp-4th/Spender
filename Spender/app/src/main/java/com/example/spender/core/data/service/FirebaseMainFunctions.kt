package com.example.spender.core.data.service

import com.google.firebase.firestore.FirebaseFirestore

fun getTotalExpense(): Int {
    val uid = getFirebaseAuth()
    var total = 0
    try {
        val ref = getFirebaseRef().document(uid!!).collection("expense")
        ref.get().addOnSuccessListener { document ->
            if (!document.isEmpty) {
                for (i in document.documents) {
                    total += i.data?.get("amount")?.toString()?.toInt() ?: 0
                }
            }
        }
    } catch (e: Exception) {
        return 0
    }
    return total
}