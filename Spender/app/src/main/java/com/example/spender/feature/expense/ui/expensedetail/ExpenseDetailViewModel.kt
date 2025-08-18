package com.example.spender.feature.expense.ui.expensedetail

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.core.data.service.getFirebaseAuth
import com.example.spender.feature.expense.data.remote.ExpenseDto
import com.example.spender.feature.expense.data.repository.ExpenseRepository
import com.example.spender.feature.expense.domain.model.Emotion
import com.example.spender.feature.expense.ui.RegistrationEvent
import com.example.spender.feature.mypage.data.repository.CategoryRepository
import com.example.spender.feature.mypage.domain.model.Category
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val expenseId: String = savedStateHandle.get<String>("expenseId")!!

    private val _uiState = MutableStateFlow(ExpenseDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _expenseCategories = MutableStateFlow<List<Category>>(emptyList())
    val expenseCategories = _expenseCategories.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RegistrationEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val emotionsList = listOf(
        Emotion(id = "satisfied", name = "만족"),
        Emotion(id = "dissatisfied", name = "불만"),
        Emotion(id = "impulsive", name = "충동"),
        Emotion(id = "unfair", name = "억울")
    )

    init {
        _uiState.update { it.copy(emotions = emotionsList) }

        loadExpenseDetails()
        loadAllExpenseCategories()
    }

    private fun loadExpenseDetails() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            val expense = expenseRepository.getExpenseById(userId, expenseId)
            if (expense != null) {
                val category = categoryRepository.getCategoryById(userId, expense.categoryId)

                _uiState.update { currentState ->
                    currentState.copy(
                        amount = expense.amount.toString(),
                        title = expense.title,
                        memo = expense.memo,
                        date = expense.date,
                        categoryId = expense.categoryId,
                        imageUrl = expense.imageUrl,
                        categoryName = category?.name ?: "미분류",
                        selectedEmotion = emotionsList.find { it.id == expense.emotion },
                        isLoading = false
                    )
                }
            } else {
                _eventFlow.emit(RegistrationEvent.ShowToast("데이터를 불러오는데 실패했습니다."))
            }
        }
    }

    private fun loadAllExpenseCategories() {
        val userId = getFirebaseAuth() ?: return
        categoryRepository.getCategories(userId, "EXPENSE") { categoryList ->
            _expenseCategories.value = categoryList
        }
    }

    // 이벤트 핸들러
    fun onAmountChange(amount: String) {
        if (amount.length <= 10) {
            _uiState.update { it.copy(amount = amount.filter { char -> char.isDigit() }) }
        }
    }

    fun onMemoChange(memo: String) {
        _uiState.update { it.copy(memo = memo) }
    }

    fun onEmotionSelected(emotion: Emotion) {
        _uiState.update { it.copy(selectedEmotion = emotion) }
    }

    fun onDateDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isDatePickerDialogVisible = isVisible) }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(categoryName = category.name, categoryId = category.id) }
    }

    fun onDateSelected(timestamp: Long?) {
        timestamp ?: return
        _uiState.update { it.copy(date = Date(timestamp)) }
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(imageUrl = uri.toString()) }
    }

    fun onImageSelectionCancelled() {
        _uiState.update { it.copy(imageUrl = null) }
    }


fun updateExpense() {
    viewModelScope.launch {
        val userId = getFirebaseAuth() ?: return@launch
        val currentState = _uiState.value
        var finalImageUrl = currentState.imageUrl

        if (currentState.imageUrl?.startsWith("content://") == true) {
            _eventFlow.emit(RegistrationEvent.ShowToast("이미지 업로드 중..."))
            val newUri = Uri.parse(currentState.imageUrl)
            finalImageUrl = uploadImageToStorage(userId, newUri)
            if (finalImageUrl == null) {
                _eventFlow.emit(RegistrationEvent.ShowToast("이미지 업로드에 실패했습니다."))
                return@launch
            }
        }

        val expenseDto = ExpenseDto(
            amount = currentState.amount.toLongOrNull() ?: 0L,
            title = currentState.title,
            memo = currentState.memo,
            date = Timestamp(currentState.date),
            categoryId = currentState.categoryId,
            emotion = currentState.selectedEmotion?.id ?: "",
            imageUrl = finalImageUrl
        )

        if (expenseRepository.updateExpense(userId, expenseId, expenseDto)) {
            _eventFlow.emit(RegistrationEvent.ShowToast("수정되었습니다"))
            _eventFlow.emit(RegistrationEvent.NavigateBack)
        } else {
            _eventFlow.emit(RegistrationEvent.ShowToast("수정에 실패했습니다."))
        }
    }
}

    private suspend fun uploadImageToStorage(userId: String, uri: Uri): String? {
        return try {
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("expenses/${userId}/${System.currentTimeMillis()}.jpg")

            imageRef.putFile(uri).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    fun deleteExpense() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            if (expenseRepository.deleteExpense(userId, expenseId)) {
                _eventFlow.emit(RegistrationEvent.ShowToast("삭제되었습니다"))
                _eventFlow.emit(RegistrationEvent.NavigateBack)
            } else {
                _eventFlow.emit(RegistrationEvent.ShowToast("삭제에 실패했습니다."))
            }
        }
    }
}