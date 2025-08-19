package com.e1i3.spender.feature.report.mapper

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.e1i3.spender.core.data.remote.report.ReportDetailDto
import com.e1i3.spender.core.data.remote.report.ReportListDto
import com.e1i3.spender.feature.report.domain.model.CategoryTotal
import com.e1i3.spender.feature.report.domain.model.EmotionTotal
import com.e1i3.spender.feature.report.domain.model.Report
import com.e1i3.spender.feature.report.domain.model.ReportDetail

object ReportMapper {
    fun fromListDto(dto: ReportListDto, index: Int): Report {
        return Report(
            id = index,
            month = dto.month,
            totalExpense = dto.totalExpense,
            totalBudget = dto.totalBudget
        )
    }

    fun fromDetailDto(dto: ReportDetailDto): ReportDetail {
        return ReportDetail(
            month = dto.month,
            totalExpense = dto.totalExpense,
            totalBudget = dto.totalBudget,
            feedback = dto.feedback,
            byCategory = dto.byCategory.map {
                CategoryTotal(
                    categoryId = it.categoryId,
                    categoryName = it.categoryName,
                    totalPrice = it.totalPrice,
                    color = Color(it.colorHex.toColorInt())
                )
            },
            byEmotion = dto.byEmotion.map {
                EmotionTotal(it.emotionId, it.amount)
            }
        )
    }
}