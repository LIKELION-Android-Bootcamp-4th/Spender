package com.example.spender.core.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.spender.R
import com.example.spender.core.widget.component.BudgetProgressBarGlance
import com.example.spender.core.data.service.getExpenseRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SpenderSmallWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val percent0to100 = withContext(Dispatchers.IO) {
            runCatching { getExpenseRate() }.getOrElse { 0f }
        }
        val percent01 = (percent0to100 / 100f).coerceIn(0f, 1f)
        val percentText = "${percent0to100.toInt()}%"

        provideContent {
            SpenderSmallWidgetContent(
                percent = percent01,
                percentText = percentText
            )
        }
    }
}


@Composable
private fun SpenderSmallWidgetContent(
    percent: Float,
    percentText: String
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(133.dp)
                .width(133.dp)
                .padding(horizontal = 10.dp)
                .background(ColorProvider(Color.White)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth().padding(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.spender_happy),
                        contentDescription = "로고",
                        modifier = GlanceModifier.size(40.dp)
                    )

                    Text("지출이", style = TextStyle(fontWeight = FontWeight.Medium))
                }
                Spacer(GlanceModifier.height(0.dp))

                BudgetProgressBarGlance(progress01 = percent)

                Spacer(GlanceModifier.height(5.dp))

                Text(percentText, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))

                Spacer(GlanceModifier.height(0.dp))

                Text(
                    text = "예산 대비 지출",
                    style = TextStyle(fontWeight = FontWeight.Normal, color = ColorProvider(Color(0x80222836)), fontSize = 13.sp)
                )

            }
        }
    }
}