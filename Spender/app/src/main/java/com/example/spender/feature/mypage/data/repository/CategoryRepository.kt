package com.example.spender.feature.mypage.data.repository

import com.example.spender.core.common.toColor
import com.example.spender.feature.mypage.domain.model.Category
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoryRepository @Inject constructor(){
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    fun getCategories(userId: String, type: String, onResult: (List<Category>) -> Unit) {
        usersCollection.document(userId).collection("categories")
            .whereEqualTo("type", type)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                val categories = snapshot?.documents?.mapNotNull { doc ->
                    val colorString = doc.getString("color")
                    Category(id = doc.id, name = doc.getString("name") ?: "", color = colorString?.toColor())
                } ?: emptyList()
                onResult(categories)
            }
    }

    fun addCategory(userId: String, categoryName: String, type: String, color: String?) {
        val newCategory = hashMapOf(
            "name" to categoryName,
            "type" to type,
            "color" to color,
            "createdAt" to FieldValue.serverTimestamp()
        )
        usersCollection.document(userId).collection("categories").add(newCategory)
    }

    fun updateCategory(userId: String, categoryId: String, newName: String) {
        usersCollection.document(userId).collection("categories").document(categoryId)
            .update("name", newName)
    }

    fun deleteCategory(userId: String, categoryId: String) {
        usersCollection.document(userId).collection("categories").document(categoryId).delete()
    }

    suspend fun getCategoryById(userId: String, categoryId: String): Category? {
        return try {
            val document = usersCollection.document(userId)
                .collection("categories")
                .document(categoryId)
                .get().await()
            val colorString = document.getString("color")
            Category(
                id = document.id,
                name = document.getString("name") ?: "",
                color = colorString?.toColor()
            )
        } catch (e: Exception) {
            null
        }
    }
}