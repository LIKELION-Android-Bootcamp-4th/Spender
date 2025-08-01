package com.example.spender.feature.report.ui.detail.component

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.HorizontalBarChart
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun EmotionBarChart(
    labels: List<String>,
    values: List<Float>,
    colors: List<Int>
) {
    AndroidView(factory = { context ->
        HorizontalBarChart(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                500
            )
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(false)
            axisLeft.isEnabled = false


            // x축 - 감정
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                setDrawAxisLine(false)
                setDrawLabels(true)
                granularity = 1f
                valueFormatter = IndexAxisValueFormatter(labels)
                textSize = 14f
            }

        }

    }, update = { chart ->
        val entries = values.mapIndexed { index, value ->
            BarEntry(index.toFloat(), value)
        }

        val dataSet = BarDataSet(entries, "").apply {
            setColors(colors)
            valueTextSize = 14f
        }

        val data = BarData(dataSet)
        chart.data = data
        chart.invalidate()
    })
}