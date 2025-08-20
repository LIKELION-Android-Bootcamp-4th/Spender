package com.e1i3.spender.core.data.remote.friend

import com.e1i3.spender.core.data.remote.report.CategoryTotalDto
import com.e1i3.spender.core.data.remote.report.EmotionTotalDto

data class FriendDetailDto(
    val nickname: String = "알 수 없음",
    val totalBudget: Int = 0,
    val totalExpense: Int = 0,
    val byCategory: List<CategoryTotalDto> = emptyList(),
    val byEmotion: List<EmotionTotalDto> = emptyList()
)