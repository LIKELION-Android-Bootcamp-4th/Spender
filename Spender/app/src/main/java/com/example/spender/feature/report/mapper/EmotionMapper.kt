package com.example.spender.feature.report.mapper


import androidx.compose.ui.graphics.Color
import com.example.spender.feature.report.domain.model.EmotionTotal
import com.example.spender.feature.report.ui.model.EmotionUiModel

val emotionLabelMap = mapOf(
    "satisfied" to "만족",
    "dissatisfied" to "불만",
    "impulsive" to "충동",
    "unfair" to "억울"
)

val emotionColorMap = mapOf(
    "satisfied" to Color(0xFFA566FF), // 만족: 보라
    "dissatisfied" to Color(0xFFFFE761), // 불만: 노랑
    "impulsive" to Color(0xFFFF7A6A), // 충동: 주황
    "unfair" to Color(0xFF36CCB3)  // 억울: 초록
)

fun List<EmotionTotal>.toUiModel(): List<EmotionUiModel> {
    val total = this.sumOf { it.amount }.takeIf { it > 0 } ?: 1

    return this.map {
        EmotionUiModel(
            label = emotionLabelMap[it.emotionId] ?: "기타",
            amount = it.amount,
            percentage = it.amount.toFloat() / total * 100,
            color = emotionColorMap[it.emotionId] ?: Color(0xFF000000)
        )
    }
}
