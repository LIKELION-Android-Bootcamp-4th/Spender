package com.example.spender.core.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class SpenderSmallWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            SpenderSmallWidgetContent()
        }
    }
}

@Composable
fun SpenderSmallWidgetContent() {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color(0xFF4CAF50))) // 초록 배경
    ) {
        Text(
            text = "이번달 남은 예산",
            style = TextStyle(color = ColorProvider(Color.White))
        )
        Text(
            text = "₩1,250,000",
            style = TextStyle(color = ColorProvider(Color.White))
        )
    }
}