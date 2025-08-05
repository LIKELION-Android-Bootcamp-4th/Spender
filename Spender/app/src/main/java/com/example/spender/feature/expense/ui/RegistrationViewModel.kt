package com.example.spender.feature.expense.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegistrationViewModel : ViewModel() {

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

    fun register() {
    }
}