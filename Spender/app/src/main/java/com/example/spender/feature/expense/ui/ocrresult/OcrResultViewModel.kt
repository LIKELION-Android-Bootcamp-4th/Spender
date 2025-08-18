package com.example.spender.feature.expense.ui.ocrresult

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
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OcrResultViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OcrResultUiState())
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
        // OCR 결과로 UiState 초기화함
        val title = savedStateHandle.get<String>("title") ?: ""
        val amount = savedStateHandle.get<String>("amount") ?: ""
        val dateString = savedStateHandle.get<String>("date") ?: ""

        val date = try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        _uiState.update {
            it.copy(
                title = title,
                amount = amount,
                date = date,
                emotions = emotionsList,
                selectedEmotion = emotionsList.first()
            )
        }
        loadExpenseCategories()
    }

    private fun loadExpenseCategories() {
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

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onMemoChange(memo: String) {
        _uiState.update { it.copy(memo = memo) }
    }

    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(categoryName = category.name, categoryId = category.id) }
    }

    fun onEmotionSelected(emotion: Emotion) {
        _uiState.update { it.copy(selectedEmotion = emotion) }
    }

    fun onDateSelected(timestamp: Long?) {
        timestamp?.let { _uiState.update { state -> state.copy(date = Date(it)) } }
    }

    fun onCategorySheetVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isCategorySheetVisible = isVisible) }
    }

    fun onDateDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isDatePickerDialogVisible = isVisible) }
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            val currentState = _uiState.value

            if (currentState.amount.isBlank() || currentState.categoryId.isBlank()) {
                _eventFlow.emit(RegistrationEvent.ShowToast("금액과 카테고리는 필수 항목입니다."))
                return@launch
            }

            val expenseDto = ExpenseDto(
                amount = currentState.amount.toLongOrNull() ?: 0L,
                title = currentState.title.ifBlank { currentState.categoryName },
                memo = currentState.memo,
                date = Timestamp(currentState.date),
                categoryId = currentState.categoryId,
                emotion = currentState.selectedEmotion?.id ?: ""
            )

            if (expenseRepository.addExpense(userId, expenseDto)) {
                _eventFlow.emit(RegistrationEvent.ShowToast("저장되었습니다"))
                _eventFlow.emit(RegistrationEvent.NavigateBack)
            } else {
                _eventFlow.emit(RegistrationEvent.ShowToast("저장에 실패했습니다."))
            }
        }
    }
}