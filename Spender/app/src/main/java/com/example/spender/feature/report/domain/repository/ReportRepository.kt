package com.example.spender.feature.report.domain.repository

import android.util.Log
import com.example.spender.core.data.remote.report.ReportListDto
import com.example.spender.core.data.service.getFirebaseAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject

class ReportRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    fun getReportList(
        year: Int,
        onSuccess: (List<ReportListDto>) -> Unit,
        onError: (Exception) -> Unit
    ){
        Log.d("ReportRepo", ">>> getReportList() 실행됨, year=$year")

        val uid = auth.currentUser?.uid
        Log.d("ReportRepo", "UID 확인용: $uid")
        if(uid == null){
            Log.d("ReportRepo", "UID가 널!!!!!")
            onError(IllegalStateException("User not logged in"))
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("reports")
            .orderBy(FieldPath.documentId()) // "2025-01", "2025-02"...
            .startAt("$year-01")
            .endAt("$year-12")
            .get()
            .addOnSuccessListener { result ->
                val summaries = result.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null

                    try {
                        ReportListDto(
                            month = doc.id, // 예: "2025-08"
                            totalExpense = (data["totalExpense"] as? Long)?.toInt() ?: 0,
                            totalBudget = (data["totalBudget"] as? Long)?.toInt() ?: 0
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                onSuccess(summaries)
            }
            .addOnFailureListener { e ->
                Log.e("ReportRepo", "Firestore getReportList 실패: ${e.message}", e)
                onError(e)
            }
    }
}