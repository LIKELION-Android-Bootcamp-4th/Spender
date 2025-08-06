package com.example.spender.feature.report.domain.repository

import android.util.Log
import com.example.spender.core.data.remote.report.CategoryTotalDto
import com.example.spender.core.data.remote.report.EmotionTotalDto
import com.example.spender.core.data.remote.report.ReportDetailDto
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
                Log.e("Report List Repo", "Firestore getReportList 실패: ${e.message}", e)
                onError(e)
            }
    }

    fun getReportDetail(
        month: String,
        onSuccess: (ReportDetailDto) -> Unit,
        onError: (Exception) -> Unit
    ){
        val uid = auth.currentUser?.uid

        if(uid == null){
            Log.d("ReportRepo", "UID가 널!!!!!")
            onError(IllegalStateException("User not logged in"))
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("reports")
            .document(month)
            .get()
            .addOnSuccessListener { result ->
                try {
                    val data = result.data ?: return@addOnSuccessListener onError(Exception("No data found"))

                    val rawByEmotion = data["by_emotion"]
                    Log.d("FirestoreCheck", "rawByEmotion = $rawByEmotion")

                    val byCategory = (data["byCategory"] as? List<Map<String, Any>>)?.mapNotNull {
                        val id = it["categoryId"] as? String ?: return@mapNotNull null
                        val price = (it["totalPrice"] as? Long)?.toInt() ?: 0
                        CategoryTotalDto(id, price)
                    }?.toList() ?: emptyList()

                    val byEmotion = (data["byEmotion"] as? List<Map<String, Any>>)?.mapNotNull {
                        val id = it["emotionId"] as? String ?: return@mapNotNull null
                        val amt = (it["amount"] as? Long)?.toInt() ?: 0
                        EmotionTotalDto(id, amt)
                    }?.toList() ?: emptyList()



                    val dto = ReportDetailDto(
                        month = result.id,
                        totalBudget = (data["totalBudget"] as? Long)?.toInt() ?: 0,
                        totalExpense = (data["totalExpense"] as? Long)?.toInt() ?: 0,
                        feedback = data["feedback"] as? String ?: "",
                        byCategory = byCategory,
                        byEmotion = byEmotion,
                    )
                    Log.d("리포트 감정 체크", "repo :  ${dto.byEmotion}")


                    onSuccess(dto)
                } catch (e: Exception) {
                    onError(e)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Report Detail Repo", "Firestore get Report Detail 실패: ${e.message}", e)
                onError(e)
            }
    }
}