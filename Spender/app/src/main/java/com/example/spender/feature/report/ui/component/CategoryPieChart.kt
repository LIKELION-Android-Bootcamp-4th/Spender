package com.example.spender.feature.report.ui.component

import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color as AndroidColor
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight


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
            if (index == maxIndex) {
                PieEntry(value, labels[index]).apply { data = index } // data 필드에 index 저장
            } else {
                PieEntry(value, "").apply { data = index }
            }
        }

        val dataSet = PieDataSet(entries, "").apply {
            setColors(colors)
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
        val highlight = Highlight(maxIndex.toFloat(), values[maxIndex], 0)
        chart.highlightValues(arrayOf(highlight))
    })
}
