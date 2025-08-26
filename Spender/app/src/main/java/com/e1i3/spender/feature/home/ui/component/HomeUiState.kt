package com.e1i3.spender.feature.home.ui.component

import com.e1i3.spender.core.data.remote.expense.ExpenseDto
import com.e1i3.spender.feature.home.domain.model.Friend

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val friends: List<Friend>,
        val tier: Int,
        val totalExpense: Int,
        val expenseRate: Float,
        val recentExpenses: List<ExpenseDto>
    ) : HomeUiState()
}
