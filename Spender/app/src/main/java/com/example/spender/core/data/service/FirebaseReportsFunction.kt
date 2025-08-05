package com.example.spender.core.data.service

import android.util.Log
import com.example.spender.core.data.remote.report.CategoryTotalDto
import com.example.spender.core.data.remote.report.EmotionTotalDto
import com.example.spender.core.data.remote.report.ReportDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import java.util.Calendar

// 해당 연도의 리포트 리스트 조회
fun getReportList(year: Int): MutableList<ReportDto> {
    val uid = getFirebaseAuth()
    val reportList = mutableListOf<ReportDto>()

    val startOfMonth = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, 0)
    }

    val endOfMonth = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, 11)
    }

    try {
        val ref = getFirebaseRef().document(uid!!).collection("expense")
            .orderBy(FieldPath.documentId())
            .startAt("$year-01")
            .endAt("$year-12")
        ref.get().addOnSuccessListener { document ->
            for (doc in document) {
                val data = doc.data
                val report = ReportDto(
                    totalBudget = data["totalBudget"]?.toString()?.toInt() ?: 0,
                    totalExpense = data["totalExpense"]?.toString()?.toInt() ?: 0,
                    feedBack = data["feedback"]?.toString() ?: "",
                    month = data["month"]?.toString() ?: doc.id.substring(4, 6),
                    byCategory = data["byCategory"] as? Array<CategoryTotalDto> ?: emptyArray(),
                    byEmotion = data["byEmotion"] as? Array<EmotionTotalDto> ?: emptyArray(),
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
                reportList.add(report)
            }
        }
    } catch (e: Exception) {
        Log.d("Reports / ReportList", "report list error")
    }
    return reportList
}

