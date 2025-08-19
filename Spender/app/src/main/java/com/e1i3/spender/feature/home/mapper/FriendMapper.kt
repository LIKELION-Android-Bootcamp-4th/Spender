package com.e1i3.spender.feature.home.mapper

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import com.e1i3.spender.core.common.toColor
import com.e1i3.spender.core.data.remote.friend.FriendDetailDto
import com.e1i3.spender.core.data.remote.friend.FriendListDto
import com.e1i3.spender.core.data.remote.report.CategoryTotalDto
import com.e1i3.spender.core.data.remote.report.EmotionTotalDto
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.feature.home.ui.model.FriendDetailUiModel
import com.e1i3.spender.feature.report.domain.model.CategoryTotal
import com.e1i3.spender.feature.report.domain.model.EmotionTotal
import com.e1i3.spender.feature.report.mapper.toUiModel
import com.e1i3.spender.feature.report.ui.model.CategoryUiModel
import com.e1i3.spender.feature.report.ui.model.EmotionUiModel

fun FriendListDto.toDomain(userId: String): Friend {
    return Friend(
        userId = userId,
        nickname = nickname,
        profileUrl = profileUrl,
        status = status,
    )
}

fun EmotionTotalDto.toDomain(): EmotionTotal {
    return EmotionTotal(
        emotionId = this.emotionId,
        amount = this.amount
    )
}

fun CategoryTotalDto.toDomain(): CategoryTotal {
    return CategoryTotal(
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        totalPrice = this.totalPrice,
        color = Color(this.colorHex.toColorInt())
    )
}


fun FriendDetailDto.toUiModel(): FriendDetailUiModel {
    val safeBudget = totalBudget.takeIf { it != 0 } ?: 1 // 0 방어
    val percentage = totalExpense / safeBudget.toFloat()

    val topCategory = byCategory.maxByOrNull { it.totalPrice }
    val categoryChartData = byCategory.map { it.toDomain() }.toUiModel()

    val topEmotion = byEmotion.maxByOrNull { it.amount }
    val emotionChartData = byEmotion.map { it.toDomain() }.toUiModel()

    return FriendDetailUiModel(
        nickname = nickname,
        budgetProgress = percentage * 100,

        topCategoryName = topCategory?.categoryName,
        categoryChartData = categoryChartData,

        topEmotionName = topEmotion?.emotionId,
        emotionChartData = emotionChartData
    )
}


