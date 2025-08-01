package com.example.spender.feature.report.ui.detail.component

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color as AndroidColor
import androidx.core.graphics.toColorInt


@Composable
fun CategoryPieChart() {
    val entries = listOf(
        PieEntry(61f, "식비"),
        PieEntry(21f, "자녀/육아"),
        PieEntry(14f, "교통"),
        PieEntry(3f, "교육/학습")
    )

    val colors = listOf(
        "#18C1AC".toColorInt(), // 식비
        "#2E9AFE".toColorInt(), // 자녀/육아
        "#8E44AD".toColorInt(), // 교통
        "#9B59B6".toColorInt()  // 교육/학습
    )

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
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = android.graphics.Color.WHITE

        val data = PieData(dataSet)
        chart.data = data
        chart.invalidate()
    })
}
