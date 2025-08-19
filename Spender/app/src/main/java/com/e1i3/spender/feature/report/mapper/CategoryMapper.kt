package com.e1i3.spender.feature.report.mapper

import com.e1i3.spender.feature.report.domain.model.CategoryTotal
import com.e1i3.spender.feature.report.ui.model.CategoryUiModel

fun List<CategoryTotal>.toUiModel(): List<CategoryUiModel> {
    val total = this.sumOf { it.totalPrice }.takeIf { it > 0 } ?: 1

    return this.map {
        CategoryUiModel(
            label = it.categoryName,
            amount = it.totalPrice,
            percentage = it.totalPrice.toFloat() / total * 100,
            color = it.color
        )
    }
}