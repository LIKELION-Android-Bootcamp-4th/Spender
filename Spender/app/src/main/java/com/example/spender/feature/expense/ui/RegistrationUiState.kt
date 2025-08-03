package com.example.spender.feature.expense.ui

data class RegistrationUiState(
    // 탭 상태
    val selectedTabIndex: Int = 1,

    // 공통 입력 상태
    val amount: String = "",
    val memo: String = "",
    val date: String = "2025.07.28", // 실제로는 ViewModel에서 오늘 날짜로 초기화 필요함
    val category: String = "취미/여가",

    // 직접 지출 상태
    val selectedEmotion: String = "만족",
    val emotions: List<String> = listOf("만족", "불만", "충동", "억울"),

    // 정기 지출 상태
    val title: String = "",
    val recurrence: String = "매월 28일",

    // 영수증 인식 상태
    val isOcrDialogVisible: Boolean = false
)