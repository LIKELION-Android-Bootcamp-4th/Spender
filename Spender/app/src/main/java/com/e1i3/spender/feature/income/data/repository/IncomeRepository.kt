package com.e1i3.spender.feature.income.data.repository

import com.e1i3.spender.feature.income.data.remote.IncomeDto
import com.e1i3.spender.feature.income.domain.medel.Income
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class IncomeRepository @Inject constructor() {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    suspend fun addIncome(userId: String, income: IncomeDto): Boolean {
        return try {
            usersCollection.document(userId).collection("incomes").add(income).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getIncomeById(userId: String, incomeId: String): Income? {
        return try {
            val document =
                usersCollection.document(userId).collection("incomes").document(incomeId).get()
                    .await()
            document.toObject(Income::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateIncome(userId: String, incomeId: String, incomeDto: IncomeDto): Boolean {
        return try {
            usersCollection.document(userId).collection("incomes").document(incomeId).set(incomeDto)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteIncome(userId: String, incomeId: String): Boolean {
        return try {
            usersCollection.document(userId).collection("incomes").document(incomeId).delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAllIncomes(userId: String): Flow<List<Income>> = callbackFlow {
        val listener = usersCollection.document(userId).collection("incomes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val incomes = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Income::class.java)?.copy(id = document.id)
                } ?: emptyList()
                trySend(incomes)
            }
        awaitClose { listener.remove() }
    }
}