package com.e1i3.spender.feature.report.ui.component

import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import android.graphics.Color as AndroidColor


@Composable
fun CategoryPieChart(
    labels: List<String>,
    values: List<Float>,
    colors: List<Int>
) {
    val sortedData = labels.indices.map { index ->
        Triple(labels[index], values[index], colors[index])
    }.sortedByDescending { it.second }

    val sortedLabels = sortedData.map { it.first }
    val sortedValues = sortedData.map { it.second }
    val sortedColors = sortedData.map { it.third }

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
        val maxIndex = sortedValues.indices.maxByOrNull { sortedValues[it] } ?: -1

        val entries = sortedValues.mapIndexed { index, value ->
            if (index == maxIndex) {
                PieEntry(value, sortedLabels[index]).apply { data = index } // data 필드에 index 저장
            } else {
                PieEntry(value, "").apply { data = index }
            }
        }

        val dataSet = PieDataSet(entries, "").apply {
            setColors(sortedColors)
            valueTextSize = 16f
            valueTextColor = AndroidColor.WHITE
            valueTypeface = Typeface.DEFAULT_BOLD

            valueFormatter = object : ValueFormatter() {
                override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                    val index = pieEntry?.data as? Int ?: return ""
                    return if (index == maxIndex) {
                        "${value.toInt()}%"
                    } else {
                        ""
                    }
                }
            }

        }

        val data = PieData(dataSet)
        chart.data = data
        chart.invalidate()
        chart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD)

        if (maxIndex != -1) {
            val highlight = Highlight(maxIndex.toFloat(), sortedValues[maxIndex], 0)
            chart.highlightValues(arrayOf(highlight))
        }


//        val highlight = Highlight(maxIndex.toFloat(), values[maxIndex], 0)
//        chart.highlightValues(arrayOf(highlight))
    })
}
