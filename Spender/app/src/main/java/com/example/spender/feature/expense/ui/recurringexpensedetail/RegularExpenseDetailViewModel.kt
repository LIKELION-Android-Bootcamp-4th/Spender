package com.example.spender.feature.expense.ui.recurringexpensedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.core.data.service.getFirebaseAuth
import com.example.spender.feature.expense.data.remote.RegularExpenseDto
import com.example.spender.feature.expense.data.repository.RegularExpenseRepository
import com.example.spender.feature.expense.ui.RegistrationEvent
import com.example.spender.feature.income.data.remote.IncomeDto
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
class RegularExpenseDetailViewModel @Inject constructor(
    private val regularExpenseRepository: RegularExpenseRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val regularExpenseId: String = savedStateHandle.get<String>("regularExpenseId")!!

    private val _uiState = MutableStateFlow(RegularExpenseDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _expenseCategories = MutableStateFlow<List<Category>>(emptyList())
    val expenseCategories = _expenseCategories.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RegistrationEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadRegularExpenseDetails()
        loadAllExpenseCategories()
    }

    private fun loadRegularExpenseDetails() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            val expense = regularExpenseRepository.getRegularExpenseById(userId, regularExpenseId)
            if (expense != null) {
                val category = categoryRepository.getCategoryById(userId, expense.categoryId)
                _uiState.update {
                    it.copy(
                        amount = expense.amount.toString(),
                        title = expense.title,
                        memo = expense.memo,
                        firstPaymentDate = expense.first_payment_date,
                        categoryId = expense.categoryId,
                        categoryName = category?.name ?: "미분류",
                        dayOfMonth = expense.day,
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

    fun onAmountChange(amount: String) {
        _uiState.update { it.copy(amount = amount) }
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
        timestamp?.let { _uiState.update { state -> state.copy(firstPaymentDate = Date(it)) } }
    }

    fun onRepeatDaySelected(day: Int) {
        _uiState.update { it.copy(dayOfMonth = day) }
    }

    fun onCategorySheetVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isCategorySheetVisible = isVisible) }
    }

    fun onDateDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isDatePickerDialogVisible = isVisible) }
    }

    fun onRepeatSheetVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isRepeatSheetVisible = isVisible) }
    }

    fun updateRegularExpense() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            val currentState = _uiState.value

            val dto = RegularExpenseDto(
                amount = currentState.amount.toLongOrNull() ?: 0L,
                title = currentState.title,
                memo = currentState.memo,
                categoryId = currentState.categoryId,
                first_payment_date = Timestamp(currentState.firstPaymentDate),
                repeat_cycle = "MONTHLY",
                day = currentState.dayOfMonth
            )

            if (regularExpenseRepository.updateRegularExpense(userId, regularExpenseId, dto)) {
                _eventFlow.emit(RegistrationEvent.ShowToast("수정되었습니다"))
                _eventFlow.emit(RegistrationEvent.NavigateBack)
            } else {
                _eventFlow.emit(RegistrationEvent.ShowToast("수정에 실패했습니다."))
            }
        }
    }

    fun deleteRegularExpense() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            if (regularExpenseRepository.deleteRegularExpense(userId, regularExpenseId)) {
                _eventFlow.emit(RegistrationEvent.ShowToast("삭제되었습니다"))
                _eventFlow.emit(RegistrationEvent.NavigateBack)
            } else {
                _eventFlow.emit(RegistrationEvent.ShowToast("삭제에 실패했습니다."))
            }
        }
    }
}