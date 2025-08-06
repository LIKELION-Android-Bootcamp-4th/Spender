package com.example.spender.feature.expense.ui

import androidx.lifecycle.ViewModel
import com.example.spender.core.data.service.getFirebaseAuth
import com.example.spender.feature.expense.data.remote.ExpenseDto
import com.example.spender.feature.expense.data.repository.ExpenseRepository
import com.example.spender.feature.mypage.data.repository.CategoryRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState = _uiState.asStateFlow()

    // 이벤트 핸들러

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

    fun onEmotionSelected(emotion: String) {
        _uiState.update { it.copy(selectedEmotion = emotion) }
    }

    fun onOcrDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isOcrDialogVisible = isVisible) }
    }

    //지출등록
    fun registerExpense() {
        val userId = getFirebaseAuth() ?: return
        val currentState = _uiState.value

        val expenseDto = ExpenseDto(
            amount = currentState.amount.toLongOrNull() ?: 0L,
            title = "직접 입력",
            memo = currentState.memo,
            date = Timestamp(Date()),
            categoryId = "temp_category_id",
            emotion = currentState.selectedEmotion
        )

        expenseRepository.addExpense(userId, expenseDto)
    }

}