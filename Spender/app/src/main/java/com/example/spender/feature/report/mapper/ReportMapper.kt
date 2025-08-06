package com.example.spender.feature.report.mapper

import com.example.spender.core.data.remote.report.ReportDetailDto
import com.example.spender.core.data.remote.report.ReportListDto
import com.example.spender.feature.report.domain.model.CategoryTotal
import com.example.spender.feature.report.domain.model.EmotionTotal
import com.example.spender.feature.report.domain.model.Report
import com.example.spender.feature.report.domain.model.ReportDetail

object ReportMapper {
    fun fromListDto(dto: ReportListDto, index: Int): Report {
        return Report(
            id = index,
            month = dto.month,
            totalExpense = dto.totalExpense,
            totalBudget = dto.totalBudget
        )
    }

    fun fromDetailDto(dto: ReportDetailDto) : ReportDetail{
        return ReportDetail(
            month = dto.month,
            totalExpense = dto.totalExpense,
            totalBudget = dto.totalBudget,
            feedback = dto.feedback,
            byCategory = dto.byCategory.map{
                CategoryTotal(it.categoryId, it.totalPrice)
            },
            byEmotion = dto.byEmotion.map {
                EmotionTotal(it.emotionId, it.amount)
            }
        )
    }
}