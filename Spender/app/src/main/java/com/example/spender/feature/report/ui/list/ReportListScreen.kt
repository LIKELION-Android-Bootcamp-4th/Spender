package com.example.spender.feature.report.ui.list

import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.spender.core.common.util.formatToManWon
import com.example.spender.feature.report.domain.model.Report
import com.example.spender.feature.report.ui.component.ReportSummaryCardHorizontal
import com.example.spender.ui.theme.LightBackgroundColor
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.navigation.Screen
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import android.graphics.Color as AndroidColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(
    navHostController: NavHostController
) {
    var currentYear by remember { mutableStateOf(2025) }
    val currentMonth = 8 // 예시용 (현재 월까지)
    val sampleReports = listOf(
        Report(1, "2025.01", 800000, 1000000),
        Report(2, "2025.02", 1300000, 1000000),
        Report(3, "2025.03", 950000, 1000000),
        Report(4, "2025.04", 1530000, 1000000),
        Report(5, "2025.05", 1190000, 1000000),
        Report(6, "2025.06", 900000, 1000000),
        Report(7, "2025.07", 800000, 1000000),
        Report(8, "2025.08", 700000, 1000000),
        Report(9, "2025.09", 1100000, 1000000),
        Report(10, "2025.10", 900000, 1000000),
        Report(11, "2025.11", 650000, 1000000),
        Report(12, "2025.12", 1200000, 1000000)
    )

    val barValues = sampleReports.map { it.totalExpense.toFloat() }
    val barLabels = sampleReports.map { it.yearMonth.substring(5) + "월" }

    val listState = rememberLazyListState()
    var selectedIndex by remember { mutableStateOf(-1) }


    LaunchedEffect(selectedIndex) {
        if (selectedIndex >= 0) {
            listState.animateScrollToItem(selectedIndex)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { currentYear-- },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "이전 년도"
                            )
                        }

                        Text(
                            text = "${currentYear}년",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { if (currentYear < 2025) currentYear++ },
                            modifier = Modifier
                                .size(36.dp)
                                .alpha(if (currentYear < 2025) 1f else 0f),
                            enabled = true
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "다음 년도"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { // TODO: 캘린더
                    }) {
                        Icon(Icons.Default.DateRange, contentDescription = "캘린더")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp)
                    .background(color = LightBackgroundColor)
            ) {
                MonthlySpendingBarChart(
                    values = barValues,
                    labels = barLabels,
                    onBarClick = { index -> selectedIndex = index }
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
                    itemsIndexed(sampleReports) { index, report ->
                        val isHighlighted = index == selectedIndex
                        ReportSummaryCardHorizontal(
                            report = report,
                            onClick = {
                                navHostController.navigate(Screen.ReportDetail.createRoute(report.id))
                            },
                            isHighlighted = isHighlighted
                        )
                    }
                }
            }
        }
    )
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
                        textColor = AndroidColor.BLACK
                    }

                    axisLeft.isEnabled = false
                    axisRight.isEnabled = false
                }
            },
            update = { chart ->
                val entries = values.mapIndexed { index, value ->
                    BarEntry(index.toFloat(), value).apply { data = index }
                }

                val mainColor = AndroidColor.rgb(49, 130, 246)

                val dataSet = BarDataSet(entries, "").apply {
                    color = mainColor
                    valueTextColor = AndroidColor.BLACK
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


                chart.renderer = RoundedVerticalBarChartRenderer(chart, chart.animator, chart.viewPortHandler)
                chart.renderer.initBuffers()
                chart.invalidate()
            }
        )
    }

}

class RoundedVerticalBarChartRenderer(
    chart: BarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val barRect = RectF()

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)

        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = dataSet.barBorderWidth

        val drawBorder = dataSet.barBorderWidth > 0f
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY
        val barData = mChart.barData
        val barWidthHalf = barData.barWidth / 2f
        val radius = 8f

        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(barData.barWidth)

        buffer.feed(dataSet)
        trans.pointValuesToPixel(buffer.buffer)

        for (j in 0 until buffer.size() step 4) {
            barRect.left = buffer.buffer[j]
            barRect.top = buffer.buffer[j + 1]
            barRect.right = buffer.buffer[j + 2]
            barRect.bottom = buffer.buffer[j + 3]

            mRenderPaint.color = dataSet.getColor(j / 4)

            c.drawRoundRect(barRect, radius, radius, mRenderPaint)

            if (drawBorder) {
                c.drawRoundRect(barRect, radius, radius, mBarBorderPaint)
            }
        }
    }
}

