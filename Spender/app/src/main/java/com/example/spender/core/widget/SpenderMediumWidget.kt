package com.example.spender.core.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class SpenderMediumWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            SpenderMediumWidgetContent()
        }
    }
}

@Composable
fun SpenderMediumWidgetContent(){
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color(0xFF2196F3))) // 파란 배경
    ) {
        Text(
            text = "최근 지출",
            style = TextStyle(color = ColorProvider(Color.White))
        )
        Text("카페 - ₩4,500", style = TextStyle(color = ColorProvider(Color.White)))
        Text("택시 - ₩12,000", style = TextStyle(color = ColorProvider(Color.White)))
        Text("마트 - ₩35,000", style = TextStyle(color = ColorProvider(Color.White)))
    }
}
