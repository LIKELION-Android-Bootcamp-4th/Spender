package com.e1i3.spender.feature.report.ui.list

import android.graphics.Canvas
import android.graphics.RectF
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.LoadingScreen
import com.e1i3.spender.feature.report.ui.component.EmptyReport
import com.e1i3.spender.feature.report.ui.component.ReportContent
import com.e1i3.spender.feature.report.ui.component.ReportTopAppBar
import com.e1i3.spender.feature.report.ui.component.YearPickerDialog
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

@Composable
fun ReportListScreen(
    navHostController: NavHostController,
    viewModel: ReportListViewModel = hiltViewModel()
) {
    val year by viewModel.currentYear
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val reports by viewModel.reportList
    val barValues = reports.map { it.totalExpense.toFloat() }
    val barLabels = reports.map { it.month.substring(5) + "월" }

    var showYearDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    var selectedIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex >= 0) listState.animateScrollToItem(selectedIndex)
    }

    LaunchedEffect(year) {
        FirebaseAuth.getInstance().currentUser?.let {
            viewModel.loadReports(year)
        }
    }

    Scaffold(
        topBar = {
            ReportTopAppBar(year = year, onPrev = viewModel::goToPreviousYear, onNext = {
                if (year < currentYear) viewModel.goToNextYear()
            }, onYearClick = { showYearDialog = true }, navController = navHostController)
        },
        content = { padding ->
            when {
                viewModel.isLoading.value -> LoadingScreen()

                reports.isEmpty() -> EmptyReport(padding)

                else -> {
                    ReportContent(
                        reports = reports,
                        barValues = barValues,
                        barLabels = barLabels,
                        selectedIndex = selectedIndex,
                        onBarClick = { index ->
                            selectedIndex = index
                        },
                        listState = listState,
                        navHostController = navHostController,
                        paddingValues = padding
                    )
                }
            }
        }
    )

    if (showYearDialog) {
        YearPickerDialog(
            currentYear = currentYear,
            selectedYear = year,
            onYearSelected = {
                viewModel.setYear(it)
                showYearDialog = false
            },
            onDismiss = { showYearDialog = false }
        )
    }
}

// 막대 그래프 둥글게 처리
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
