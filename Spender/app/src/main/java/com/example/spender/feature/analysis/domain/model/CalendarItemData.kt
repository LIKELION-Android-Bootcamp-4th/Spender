package com.example.spender.feature.analysis.domain.model

data class CalendarItemData(
    val day: Int = 0,
    val expense: Int = 0,
    val background: Boolean = false,
    val today: Boolean = false
)