package com.e1i3.spender.feature.home.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.core.data.service.getFirebaseAuth
import com.e1i3.spender.feature.expense.data.repository.ExpenseRepository
import com.e1i3.spender.feature.expense.domain.model.Expense
import com.e1i3.spender.feature.home.domain.model.Transaction
import com.e1i3.spender.feature.home.ui.SearchUiState
import com.e1i3.spender.feature.income.data.repository.IncomeRepository
import com.e1i3.spender.feature.income.domain.medel.Income
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val incomeRepository: IncomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val allExpenses = expenseRepository.getAllExpenses(getFirebaseAuth() ?: "")
    private val allIncomes = incomeRepository.getAllIncomes(getFirebaseAuth() ?: "")

    val searchResults: StateFlow<List<Transaction>> =
        combine(
            uiState,
            allExpenses,
            allIncomes
        ) { state, expenses, incomes ->
            if (state.searchQuery.isBlank()) {
                return@combine emptyList<Transaction>()
            }

            val sourceList = if (state.selectedTabIndex == 0) expenses else incomes

            sourceList
                .filter { item ->
                    when(item) {
                        is Expense -> item.title.contains(state.searchQuery, ignoreCase = true)
                        is Income -> item.title.contains(state.searchQuery, ignoreCase = true)
                        else -> false
                    }
                }
                .sortedByDescending { item ->
                    when(item) {
                        is Expense -> item.date
                        is Income -> item.date
                        else -> Date()
                    }
                }
                .map { item ->
                    if (item is Expense) {
                        Transaction(item.id, item.title, item.amount, "EXPENSE", item.date)
                    } else {
                        Transaction((item as Income).id, item.title, item.amount, "INCOME", item.date)
                    }
                }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }


}