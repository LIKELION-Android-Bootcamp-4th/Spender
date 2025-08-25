package com.e1i3.spender.feature.home.domain.repository

import com.e1i3.spender.feature.home.mapper.toTierHistoryDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class TierHistoryRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    suspend fun getTierHistoryList(year: Int) = runCatching {
        val uid = auth.currentUser?.uid ?: error("로그아웃 상태")

        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("tiers")
            .orderBy(FieldPath.documentId())
            .whereGreaterThanOrEqualTo(FieldPath.documentId(), "$year-01")
            .whereLessThanOrEqualTo(FieldPath.documentId(), "$year-12\uf8ff")
            .get()
            .await()

        snapshot.documents.map { document ->
            document.toTierHistoryDto()
        }
    }
}