package com.e1i3.spender.feature.report.domain.repository

import com.e1i3.spender.core.data.remote.report.CategoryTotalDto
import com.e1i3.spender.core.data.remote.report.EmotionTotalDto
import com.e1i3.spender.core.data.remote.report.ReportDetailDto
import com.e1i3.spender.core.data.remote.report.ReportListDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class ReportRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getReportList(year: Int) = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val querySnapshot = firestore.collection("users")
            .document(uid)
            .collection("reports")
            .orderBy(FieldPath.documentId())
            .startAt("$year-01")
            .endAt("$year-12")
            .get()
            .await()

        val summaries = querySnapshot.documents.mapNotNull { doc ->
            val data = doc.data ?: return@mapNotNull null
            try {
                ReportListDto(
                    month = doc.id,
                    totalExpense = (data["totalExpense"] as? Number)?.toInt() ?: 0,
                    totalBudget = (data["totalBudget"] as? Number)?.toInt() ?: 0
                )
            } catch (e: Exception) {
                null
            }
        }
        summaries
    }

    suspend fun getReportDetail(month: String) = runCatching {

        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val documentSnapshot = firestore.collection("users")
            .document(uid)
            .collection("reports")
            .document(month)
            .get()
            .await()

        val data = documentSnapshot.data ?: error("데이터 없음")

        val byCategory = (data["byCategory"] as? List<Map<String, Any>>)?.mapNotNull {
            val id = it["categoryId"] as? String ?: return@mapNotNull null
            val name = it["categoryName"] as? String ?: return@mapNotNull null
            val price = (it["totalPrice"] as? Number)?.toInt() ?: 0
            val color = it["color"] as? String ?: return@mapNotNull null
            CategoryTotalDto(id, name, price, color)
        } ?: emptyList()

        val byEmotion = (data["byEmotion"] as? List<Map<String, Any>>)?.mapNotNull {
            val id = it["emotionId"] as? String ?: return@mapNotNull null
            val amt = (it["amount"] as? Number)?.toInt() ?: 0
            EmotionTotalDto(id, amt)
        } ?: emptyList()

        ReportDetailDto(
            month = documentSnapshot.id,
            tier = (data["tier"] as? Number)?.toInt() ?: 3,
            totalBudget = (data["totalBudget"] as? Number)?.toInt() ?: 0,
            totalExpense = (data["totalExpense"] as? Number)?.toInt() ?: 0,
            feedback = data["feedback"] as? String ?: "",
            byCategory = byCategory,
            byEmotion = byEmotion,
        )
    }
}