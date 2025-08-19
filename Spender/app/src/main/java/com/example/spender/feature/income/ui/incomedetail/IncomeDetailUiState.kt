package com.example.spender.feature.income.ui.incomedetail

import java.util.Date

data class IncomeDetailUiState(
    val isLoading: Boolean = true,
    val amount: String = "",
    val title: String = "",
    val memo: String = "",
    val date: Date = Date(),
    val categoryId: String = "",
    val categoryName: String = "",
    val isDatePickerDialogVisible: Boolean = false,
)