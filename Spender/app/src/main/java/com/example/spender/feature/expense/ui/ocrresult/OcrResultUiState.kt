package com.example.spender.feature.expense.ui.ocrresult

import com.example.spender.feature.expense.domain.model.Emotion
import java.util.Date

data class OcrResultUiState(
    val amount: String = "",
    val title: String = "",
    val date: Date = Date(),
    val memo: String = "",
    val categoryId: String = "",
    val categoryName: String = "카테고리 선택",
    val selectedEmotion: Emotion? = null,
    val emotions: List<Emotion> = emptyList(),

    val isLoading: Boolean = false,
    val isCategorySheetVisible: Boolean = false,
    val isDatePickerDialogVisible: Boolean = false
)