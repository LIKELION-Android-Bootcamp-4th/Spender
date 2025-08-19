package com.e1i3.spender.core.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class SpenderSmallReceiverWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SpenderSmallWidget()
}