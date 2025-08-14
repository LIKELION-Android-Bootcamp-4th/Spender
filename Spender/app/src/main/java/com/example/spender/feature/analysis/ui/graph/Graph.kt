package com.example.spender.feature.analysis.ui.graph

import android.icu.text.DecimalFormat
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.spender.feature.analysis.domain.model.CalendarItemData
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun LineChart(sampleData: List<CalendarItemData>) {
    Column {
        var graphData = sampleData
        val total = graphData.sumOf { it.expense }
        val avg = graphData.sumOf { it.expense } / 31

        val data = mutableListOf<CalendarItemData>()
        for (i in 1..31) {
            val graph = graphData.filter { it.day == i }
            if (graph.isNotEmpty()) {
                data.add(graph.first())
                continue
            }
            data.add(CalendarItemData(i, 0))
        }

        val dataSet =
            LineDataSet(data.map { Entry(it.day.toFloat(), it.expense.toFloat()) }.toList(), "지출")
        dataSet.apply {
            lineWidth = 2f
            color = PointColor.toArgb()
            setDrawValues(false)
            setDrawFilled(false)
            setDrawCircles(false)
            mode = LineDataSet.Mode.LINEAR
        }

        val labelColor = MaterialTheme.colorScheme.onBackground.toArgb()

        AndroidView(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.5f),
            factory = { context ->
                LineChart(context).apply {
                    dataSet.lineWidth = 2f
                    this.data = LineData(dataSet)
                    description.isEnabled = false
                    legend.isEnabled = false
                    setTouchEnabled(false)
                    setScaleEnabled(false)
                    setDrawGridBackground(false)
                    setDrawBorders(false)
                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return "${value.toInt()}일"
                            }
                        }
                        setDrawGridLines(false)
                        textColor = labelColor
                    }
                    axisLeft.apply {
                        setDrawGridLines(true)
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                val small = value.toInt() % 10000 / 1000
                                return if (small != 0) "${value.toInt() / 10000}만 ${value.toInt() % 10000 / 1000}천원"
                                else "${value.toInt() / 10000}만원"
                            }
                        }
                        textColor = labelColor
                    }
                    axisRight.isEnabled = false
                }
            },
            update = { chart ->
                chart.data = LineData(dataSet)
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
        )

        Spacer(Modifier.height(10.dp))
        Row(Modifier.padding(horizontal = 5.dp)) {
            Text("총 지출", style = Typography.labelMedium)
            Spacer(Modifier.weight(1f))
            Text("${DecimalFormat("#,###").format(total)}원", style = Typography.titleSmall)
            Spacer(Modifier.width(5.dp))
        }
        Spacer(Modifier.height(3.dp))
        Row(Modifier.padding(horizontal = 10.dp)) {
            Spacer(Modifier.weight(1f))
            Text("이번 달 평균 ${DecimalFormat("#,###").format(avg)}원", style = Typography.labelSmall)
        }
    }
}