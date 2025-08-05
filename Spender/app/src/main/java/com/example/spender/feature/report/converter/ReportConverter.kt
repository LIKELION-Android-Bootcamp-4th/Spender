package com.example.spender.feature.report.converter

import com.example.spender.core.data.remote.report.ReportListDto
import com.example.spender.feature.report.domain.model.Report

object ReportConverter {
    fun fromDto(dto: ReportListDto, index: Int): Report {
        return Report(
            id = index,
            month = dto.month,
            totalExpense = dto.totalExpense,
            totalBudget = dto.totalBudget
        )
    }
}