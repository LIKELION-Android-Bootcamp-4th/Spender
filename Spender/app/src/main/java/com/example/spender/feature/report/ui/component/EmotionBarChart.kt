package com.example.spender.feature.report.ui.component

import android.graphics.Canvas
import android.graphics.RectF
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

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
                setDrawAxisLine(true)
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
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.setDrawGridLines(false)
        chart.renderer = RoundedBarChartRenderer(chart, chart.animator, chart.viewPortHandler)
        chart.invalidate()
    })
}


class RoundedBarChartRenderer(
    chart: HorizontalBarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : HorizontalBarChartRenderer(chart, animator, viewPortHandler) {

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

        val isCustomRounded = true
        val radius = 40f

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

//            if (isCustomRounded) {
//                c.drawRoundRect(barRect, radius, radius, mRenderPaint)
//            } else {
//                c.drawRect(barRect, mRenderPaint)
//            }

            if (drawBorder) {
                c.drawRoundRect(barRect, radius, radius, mBarBorderPaint)
            }
        }
    }
}
