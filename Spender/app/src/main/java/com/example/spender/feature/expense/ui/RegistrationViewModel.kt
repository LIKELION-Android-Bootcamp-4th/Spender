package com.example.spender.feature.expense.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.core.data.service.getFirebaseAuth
import com.example.spender.feature.expense.data.remote.ExpenseDto
import com.example.spender.feature.expense.data.repository.ExpenseRepository
import com.example.spender.feature.expense.domain.model.Emotion
import com.example.spender.feature.mypage.data.repository.CategoryRepository
import com.example.spender.feature.mypage.domain.model.Category
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

sealed class RegistrationEvent {
    data class ShowToast(val message: String) : RegistrationEvent()
//    data object NavigateBack : RegistrationEvent()
}

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
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
        _uiState.update {
            it.copy(
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

    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(category = category.name, categoryId = category.id) }
    }


    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun onAmountChange(newAmount: String) {
        if (newAmount.length <= 10) {
            _uiState.update { it.copy(amount = newAmount.filter { char -> char.isDigit() }) }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onMemoChange(newMemo: String) {
        _uiState.update { it.copy(memo = newMemo) }
    }

    fun onEmotionSelected(emotion: Emotion) {
        _uiState.update { it.copy(selectedEmotion = emotion) }
    }

    fun onOcrDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isOcrDialogVisible = isVisible) }
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            when (_uiState.value.selectedTabIndex) {
                1 -> registerExpense()
                2 -> registerRecurringExpense()
            }
        }
    }
    //지출등록
    private suspend fun registerExpense() {
        val userId = getFirebaseAuth() ?: return
        val currentState = _uiState.value

        if (currentState.amount.isBlank() || currentState.categoryId.isBlank()) {
            _eventFlow.emit(RegistrationEvent.ShowToast("금액과 카테고리는 필수 항목입니다."))
            return
        }

        val expenseDto = ExpenseDto(
            amount = currentState.amount.toLongOrNull() ?: 0L,
            title = currentState.title,
            memo = currentState.memo,
            date = Timestamp(currentState.date),
            categoryId = currentState.categoryId,
            emotion = currentState.selectedEmotion?.id ?: emotionsList.first().id
        )

        val isSuccess = expenseRepository.addExpense(userId, expenseDto)
        if (isSuccess) {
            _eventFlow.emit(RegistrationEvent.ShowToast("저장되었습니다"))
            clearInputs()
//            _eventFlow.emit(RegistrationEvent.NavigateBack)
        } else {
            _eventFlow.emit(RegistrationEvent.ShowToast("저장에 실패했습니다."))
        }
    }

    private fun registerRecurringExpense() {
        // TODO: 정기 지출 등록 로직 구현
    }

    // 지출등록 후 필드 초기화
    private fun clearInputs() {
        _uiState.update {
            it.copy(
                title = "",
                amount = "",
                memo = "",
                category = "카테고리 선택",
                categoryId = "",
                selectedEmotion = emotionsList.first()
            )
        }
    }

    fun onDateSelected(timestamp: Long?) {
        timestamp ?: return
        _uiState.update { it.copy(date = Date(timestamp)) }
    }

    fun onDateDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isDatePickerDialogVisible = isVisible) }
    }
}