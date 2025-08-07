package com.example.spender.feature.mypage.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.spender.core.common.toHexString
import com.example.spender.core.data.service.getFirebaseAuth
import com.example.spender.feature.mypage.data.repository.CategoryRepository
import com.example.spender.feature.mypage.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val categoryColors = listOf(
        Color(0xFFF07463), Color(0xFF3182F6), Color(0xFFBF7F5C), Color(0xFF8C637C),
        Color(0xFFFFC107), Color(0xFFA86EF4), Color(0xFFF48AE6), Color(0xFF95C8C5),
        Color(0xFF4EAA07), Color(0xFF33F2CB)
    )

    fun loadCategories(type: String) {
        val userId = getFirebaseAuth() ?: return
        repository.getCategories(userId, type) { categoryList ->
            _categories.value = categoryList
        }
    }

    fun addCategory(name: String, type: String) {
        val userId = getFirebaseAuth() ?: return
        val newColor: Color? = if (type == "EXPENSE") {
            categoryColors.getOrNull(_categories.value.size)
        } else {
            null
        }
        repository.addCategory(userId, name, type, newColor?.toHexString())
    }

    fun updateCategory(id: String, newName: String) {
        val userId = getFirebaseAuth() ?: return
        repository.updateCategory(userId, id, newName)
    }

    fun deleteCategory(id: String) {
        val userId = getFirebaseAuth() ?: return
        repository.deleteCategory(userId, id)
    }
}