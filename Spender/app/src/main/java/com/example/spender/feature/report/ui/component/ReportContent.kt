package com.example.spender.feature.report.ui.component

import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.spender.core.common.util.formatToManWon
import com.example.spender.feature.report.domain.model.Report
import com.example.spender.feature.report.ui.list.RoundedVerticalBarChartRenderer
import com.example.spender.ui.theme.LightBackgroundColor
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.navigation.Screen
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

@Composable
fun ReportContent(
    reports : List<Report>,
    barValues : List<Float>,
    barLabels : List<String>,
    selectedIndex : Int,
    onBarClick: (Int) -> Unit,
    listState: LazyListState,
    navHostController: NavHostController,
    paddingValues: PaddingValues
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 40.dp)
            .background(color = LightBackgroundColor)
    ) {
        MonthlySpendingBarChart(
            values = barValues,
            labels = barLabels,
            onBarClick = onBarClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "리포트 보러가기", style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(reports) { index, report ->
                val isHighlighted = index == selectedIndex
                ReportSummaryCardHorizontal(
                    report = report,
                    onClick = {
                        navHostController.navigate(
                            Screen.ReportDetail.createRoute(report.month)
                        )
                    },
                    isHighlighted = isHighlighted
                )
            }
        }
    }
}

@Composable
fun MonthlySpendingBarChart(
    values: List<Float>,
    labels: List<String>,
    onBarClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
            )
            .padding(top = 24.dp)
    ) {
        AndroidView(
            factory = { context ->
                BarChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        500
                    )

                    description.isEnabled = false
                    legend.isEnabled = false
                    setTouchEnabled(true)
                    isHighlightPerTapEnabled = true
                    isDoubleTapToZoomEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(true)

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        granularity = 1f
                        valueFormatter = IndexAxisValueFormatter(labels)
                        textColor = android.graphics.Color.BLACK
                    }

                    axisLeft.isEnabled = false
                    axisRight.isEnabled = false
                }
            },
            update = { chart ->
                val entries = values.mapIndexed { index, value ->
                    BarEntry(index.toFloat(), value).apply { data = index }
                }

                val mainColor = android.graphics.Color.rgb(49, 130, 246)

                val dataSet = BarDataSet(entries, "").apply {
                    color = mainColor
                    valueTextColor = android.graphics.Color.BLACK
                    valueTextSize = 0f
                    valueTypeface = Typeface.DEFAULT_BOLD
                }

                chart.data = BarData(dataSet).apply {
                    barWidth = 0.5f
                    setValueFormatter(object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return "${formatToManWon(value.toInt())}"
                        }
                    })
                }

                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        val index = when {
                            e?.data is Int -> e.data as Int
                            e?.x != null -> e.x.toInt()
                            else -> return
                        }
                        onBarClick(index)
                    }

                    override fun onNothingSelected() {}
                })


                chart.renderer =
                    RoundedVerticalBarChartRenderer(chart, chart.animator, chart.viewPortHandler)
                chart.renderer.initBuffers()
                chart.invalidate()
            }
        )
    }

}