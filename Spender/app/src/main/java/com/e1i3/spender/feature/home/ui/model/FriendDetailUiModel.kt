package com.e1i3.spender.feature.home.ui.model

import androidx.compose.ui.graphics.Color
import com.e1i3.spender.feature.report.ui.model.CategoryUiModel
import com.e1i3.spender.feature.report.ui.model.EmotionUiModel

data class FriendDetailUiModel(
    val nickname: String = "",
    val budgetProgress: Float = 0f,

    val topCategoryName: String? = null,
    val topCategoryColor: Color,
    val categoryChartData: List<CategoryUiModel> = emptyList(),

    val topEmotionName: String? = null,
    val topEmotionColor: Color,
    val emotionChartData: List<EmotionUiModel> = emptyList()
)
