package com.example.spender.core.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class SpenderMiniReceiverWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SpenderMiniWidget()
}