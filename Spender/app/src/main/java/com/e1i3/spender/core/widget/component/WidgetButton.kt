package com.e1i3.spender.core.widget.component

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.e1i3.spender.ui.theme.PointColor


@Composable
fun WidgetButton(
    text: String,
    height: Dp? = null,
    onClickIntent: Intent,
    modifier: GlanceModifier = GlanceModifier
){
    val base = if (height != null) modifier.height(height) else modifier

    Box(
        modifier = base
            .height(40.dp)
            .background(ColorProvider(PointColor))
            .cornerRadius(8.dp)
            .padding(horizontal = 0.dp, vertical = 8.dp)
            .clickable(onClick = actionStartActivity(onClickIntent)),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}