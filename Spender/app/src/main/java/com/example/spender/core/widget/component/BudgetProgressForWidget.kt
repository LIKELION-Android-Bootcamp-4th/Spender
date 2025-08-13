package com.example.spender.core.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.height
import androidx.glance.layout.width
import com.example.spender.ui.theme.PointColor

@Composable
fun BudgetProgressBarGlance(
    progress01: Float,
    barHeightDp: Int = 12,
    baseWidthDp: Int = 100,
) {
    val p = progress01.coerceIn(0f, 1f)
    val fillWidth = (baseWidthDp * p).dp

    Box(
        modifier = GlanceModifier
            .width(baseWidthDp.dp)
            .height(barHeightDp.dp)
            .cornerRadius(999.dp)
            .background(androidx.glance.unit.ColorProvider(Color(0xFFF0F0F0)))
    ) {
        Box(
            modifier = GlanceModifier
                .width(fillWidth)
                .height(barHeightDp.dp)
                .cornerRadius(999.dp)
                .background(androidx.glance.unit.ColorProvider(PointColor))
        ){

        }
    }
}
