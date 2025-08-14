package com.example.spender.core.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class SpenderLargeWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            SpenderLargeWidgetContent()
        }
    }
}

@Composable
fun SpenderLargeWidgetContent(){
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color(0xFFFF9800))) // 주황 배경
            .padding(16.dp)
    ) {
        Text(
            text = "빠른 추가",
            style = TextStyle(color = ColorProvider(Color.White))
        )
        Row {
            Text("➕ 수입", style = TextStyle(color = ColorProvider(Color.White)))
            Text("➖ 지출", style = TextStyle(color = ColorProvider(Color.White)))
        }
    }
}