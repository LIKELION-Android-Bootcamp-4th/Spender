package com.e1i3.spender.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.core.data.service.getFirebaseAuth
import com.e1i3.spender.feature.expense.data.repository.ExpenseRepository
import com.e1i3.spender.feature.home.domain.model.Transaction
import com.e1i3.spender.feature.income.data.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val incomeRepository: IncomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        search()
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
        search()
    }

    private fun search() {
        viewModelScope.launch {
            val userId = getFirebaseAuth() ?: return@launch
            val query = _uiState.value.searchQuery
            if (query.isBlank()) {
                _uiState.update { it.copy(searchResults = emptyList()) }
                return@launch
            }

            val results = if (_uiState.value.selectedTabIndex == 0) {
                expenseRepository.searchExpenses(userId, query).map {
                    Transaction(it.id, it.title, it.amount, "EXPENSE", it.date)
                }
            } else {
                incomeRepository.searchIncomes(userId, query).map {
                    Transaction(it.id, it.title, it.amount, "INCOME", it.date)
                }
            }
            _uiState.update { it.copy(searchResults = results) }
        }
    }
}