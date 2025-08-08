package com.example.spender.feature.income.ui

import java.util.Date

data class IncomeRegistrationUiState(
    val amount: String = "",
    val title: String = "",
    val memo: String = "",
    val date: Date = Date(),
    val category: String = "카테고리 선택",
    val categoryId: String = "",
    val isDatePickerDialogVisible: Boolean = false,
)