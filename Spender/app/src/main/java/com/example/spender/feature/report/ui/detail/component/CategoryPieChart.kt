package com.example.spender.feature.report.ui.detail.component

import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color as AndroidColor
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.formatter.ValueFormatter


@Composable
fun CategoryPieChart(
    labels: List<String>,
    values: List<Float>,
    colors: List<Int>
) {
    AndroidView(factory = { context ->
        PieChart(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                500
            )
            setUsePercentValues(true)
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(AndroidColor.WHITE)
            setTransparentCircleAlpha(0)
            legend.isEnabled = false
        }
    }, update = { chart ->
        val maxIndex = values.indices.maxBy { values[it] }

        val entries = values.mapIndexed { index, value ->
//            if (index == maxIndex) {
//                PieEntry(value, labels[index])
//            } else {
//                PieEntry(value, "")
//            }
            PieEntry(value, labels[index])
        }

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
            valueTextSize = 0f
            valueTextColor = AndroidColor.WHITE
            valueTypeface = Typeface.DEFAULT_BOLD
        }

        val data = PieData(dataSet)
        chart.data = data
        chart.invalidate()
    })
}
