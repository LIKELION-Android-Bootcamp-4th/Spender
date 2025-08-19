package com.e1i3.spender.feature.mypage.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.e1i3.spender.core.common.toHexString
import com.e1i3.spender.core.data.service.getFirebaseAuth
import com.e1i3.spender.feature.mypage.data.repository.CategoryRepository
import com.e1i3.spender.feature.mypage.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val categoryColors = listOf(
        Color(0xFF0FC9BA), Color(0xFF199DF0), Color(0xFF6378EC), Color(0xFFA17FF0),
        Color(0xFFD47DF0), Color(0xFFFF6EC7), Color(0xFFFF7F65), Color(0xFFF5A562),
        Color(0xFFF7D66C), Color(0xFFB8E986)
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