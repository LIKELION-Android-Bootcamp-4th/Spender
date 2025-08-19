package com.e1i3.spender.feature.income.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.core.data.service.getFirebaseAuth
import com.e1i3.spender.feature.expense.ui.RegistrationEvent
import com.e1i3.spender.feature.income.data.remote.IncomeDto
import com.e1i3.spender.feature.income.data.repository.IncomeRepository
import com.e1i3.spender.feature.mypage.data.repository.CategoryRepository
import com.e1i3.spender.feature.mypage.domain.model.Category
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class IncomeRegistrationViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeRegistrationUiState())
    val uiState = _uiState.asStateFlow()

    private val _incomeCategories = MutableStateFlow<List<Category>>(emptyList())
    val incomeCategories = _incomeCategories.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RegistrationEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadIncomeCategories()
    }

    private fun loadIncomeCategories() {
        val userId = getFirebaseAuth() ?: return
        categoryRepository.getCategories(userId, "INCOME") { categoryList ->
            _incomeCategories.value = categoryList
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
        _uiState.update { it.copy(category = category.name, categoryId = category.id) }
    }

    fun onDateSelected(timestamp: Long?) {
        timestamp ?: return
        _uiState.update { it.copy(date = Date(timestamp)) }
    }

    fun onDateDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isDatePickerDialogVisible = isVisible) }
    }

    //수입 등록
    fun onRegisterClick() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            val currentState = _uiState.value

            if (currentState.amount.isBlank() || currentState.categoryId.isBlank()) {
                _eventFlow.emit(RegistrationEvent.ShowToast("금액과 카테고리는 필수 항목입니다."))
                return@launch
            }

            val incomeDto = IncomeDto(
                amount = currentState.amount.toLongOrNull() ?: 0L,
                title = currentState.title,
                memo = currentState.memo,
                date = Timestamp(currentState.date),
                categoryId = currentState.categoryId
            )

            val isSuccess = incomeRepository.addIncome(userId, incomeDto)
            if (isSuccess) {
                _eventFlow.emit(RegistrationEvent.ShowToast("저장되었습니다"))
                clearInputs()
                _eventFlow.emit(RegistrationEvent.NavigateBack)
            } else {
                _eventFlow.emit(RegistrationEvent.ShowToast("저장에 실패했습니다."))
            }
        }
    }

    private fun clearInputs() {
        _uiState.update {
            it.copy(
                amount = "",
                title = "",
                memo = "",
                category = "카테고리 선택",
                categoryId = ""
            )
        }
    }
}