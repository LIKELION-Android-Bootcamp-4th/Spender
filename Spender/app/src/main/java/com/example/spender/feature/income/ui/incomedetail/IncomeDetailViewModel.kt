package com.example.spender.feature.income.ui.incomedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.core.data.service.getFirebaseAuth
import com.example.spender.feature.expense.ui.RegistrationEvent
import com.example.spender.feature.income.data.remote.IncomeDto
import com.example.spender.feature.income.data.repository.IncomeRepository
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
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class IncomeDetailViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val incomeId: String = savedStateHandle.get<String>("incomeId")!!

    private val _uiState = MutableStateFlow(IncomeDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _incomeCategories = MutableStateFlow<List<Category>>(emptyList())
    val incomeCategories = _incomeCategories.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RegistrationEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadIncomeDetails()
        loadAllIncomeCategories()
    }

    private fun loadIncomeDetails() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            val income = incomeRepository.getIncomeById(userId, incomeId)
            if (income != null) {
                val category = categoryRepository.getCategoryById(userId, income.categoryId)
                _uiState.update { currentState ->
                    currentState.copy(
                        amount = income.amount.toString(),
                        title = income.title,
                        memo = income.memo,
                        date = income.date,
                        categoryId = income.categoryId,
                        categoryName = category?.name ?: "미분류",
                        isLoading = false
                    )
                }
            } else {
                _eventFlow.emit(RegistrationEvent.ShowToast("데이터를 불러오는데 실패했습니다."))
            }
        }
    }

    private fun loadAllIncomeCategories() {
        val userId = getFirebaseAuth() ?: return
        categoryRepository.getCategories(userId, "INCOME") { categoryList ->
            _incomeCategories.value = categoryList
        }
    }

    fun onAmountChange(amount: String) {
        _uiState.update { it.copy(amount = amount.filter { c -> c.isDigit() }) }
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

    fun onDateSelected(timestamp: Long?) {
        timestamp?.let { _uiState.update { state -> state.copy(date = Date(it)) } }
    }

    fun onDateDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isDatePickerDialogVisible = isVisible) }
    }

    fun updateIncome() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            val currentState = _uiState.value

            val incomeDto = IncomeDto(
                amount = currentState.amount.toLongOrNull() ?: 0L,
                title = currentState.title,
                memo = currentState.memo,
                date = Timestamp(currentState.date),
                categoryId = currentState.categoryId
            )

            if (incomeRepository.updateIncome(userId, incomeId, incomeDto)) {
                _eventFlow.emit(RegistrationEvent.ShowToast("수정되었습니다"))
                _eventFlow.emit(RegistrationEvent.NavigateBack)
            } else {
                _eventFlow.emit(RegistrationEvent.ShowToast("수정에 실패했습니다."))
            }
        }
    }

    fun deleteIncome() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            if (incomeRepository.deleteIncome(userId, incomeId)) {
                _eventFlow.emit(RegistrationEvent.ShowToast("삭제되었습니다"))
                _eventFlow.emit(RegistrationEvent.NavigateBack)
            } else {
                _eventFlow.emit(RegistrationEvent.ShowToast("삭제에 실패했습니다."))
            }
        }
    }
}