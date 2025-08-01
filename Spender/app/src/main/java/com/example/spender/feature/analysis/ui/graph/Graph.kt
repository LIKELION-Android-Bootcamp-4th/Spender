package com.example.spender.feature.analysis.ui.graph

import androidx.compose.runtime.Composable
import com.example.spender.feature.analysis.domain.model.CalendarItemData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun LineChart() {
    val sampleData = listOf( //테스트용
        CalendarItemData(5, 10000),
        CalendarItemData(10, 20000),
        CalendarItemData(20, 35000),
        CalendarItemData(31, 15000)
    )

    val dataSet = LineDataSet(sampleData.map { Entry(it.day.toFloat(), it.expense.toFloat()) }.toList(), "지출")
}