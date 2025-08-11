package com.example.spender.feature.expense.ui.expensedetail

import com.example.spender.feature.expense.domain.model.Emotion
import java.util.Date

data class ExpenseDetailUiState(
    val amount: String = "",
    val title: String = "",
    val memo: String = "",
    val date: Date = Date(),
    val categoryId: String = "",
    val selectedEmotion: Emotion? = null,
    val isLoading: Boolean = true,
    val categoryName: String = "",
    val isDatePickerDialogVisible: Boolean = false,
    val emotions: List<Emotion> = emptyList(),
)