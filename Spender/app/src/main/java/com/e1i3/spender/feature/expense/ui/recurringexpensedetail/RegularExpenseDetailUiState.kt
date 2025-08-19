package com.e1i3.spender.feature.expense.ui.recurringexpensedetail

import java.util.Date

data class RegularExpenseDetailUiState(
    val isLoading: Boolean = true,
    val amount: String = "",
    val title: String = "",
    val memo: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val firstPaymentDate: Date = Date(),
    val dayOfMonth: Int = 1,
    val isDatePickerDialogVisible: Boolean = false,
    val isCategorySheetVisible: Boolean = false,
    val isRepeatSheetVisible: Boolean = false
)