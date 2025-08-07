package com.example.spender.feature.expense.ui

import com.example.spender.feature.expense.domain.model.Emotion
import java.util.Date

data class RegistrationUiState(
    // 탭 상태
    val selectedTabIndex: Int = 1,

    // 공통 입력 상태
    val amount: String = "",
    val memo: String = "",
    val date: Date = Date(),
    val isDatePickerDialogVisible: Boolean = false,
    val category: String = "카테고리 선택",
    val categoryId: String = "",


    // 직접 지출 상태
    val selectedEmotion: Emotion? = null,
    val emotions: List<Emotion> = emptyList(),

    // 정기 지출 상태
    val title: String = "",
    val recurrence: String = "매월 28일",

    // 영수증 인식 상태
    val isOcrDialogVisible: Boolean = false
)