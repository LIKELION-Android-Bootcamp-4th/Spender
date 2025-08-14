package com.example.spender.core.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.spender.MainActivity
import com.example.spender.R

class SpenderMiniWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            SpenderMiniWidgetContent()
        }
    }
}

private fun deepLinkToExpenseRegistration(context: Context): Intent =
    Intent(context, MainActivity::class.java).apply {
        action = Intent.ACTION_VIEW
        data = "spender://expense_registration".toUri()
        addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        )
    }

@Composable
fun SpenderMiniWidgetContent() {
    Column(
        modifier = GlanceModifier
            .clickable(onClick = actionStartActivity(deepLinkToExpenseRegistration(LocalContext.current)))
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = GlanceModifier
                .size(40.dp)
                .cornerRadius(999.dp)
                .background(
                    ColorProvider(
                        day = Color.White,
                        night = Color(0xFF2C2C2C)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // 중앙 아이콘 (벡터면 tint로 색 입히기 가능)
            Image(
                provider = ImageProvider(R.drawable.spender_happy),
                contentDescription = "지출 등록 아이콘",
                modifier = GlanceModifier.size(32.dp)
            )

            Column(modifier = GlanceModifier.fillMaxSize()) {
                Spacer(GlanceModifier.height(0.dp).fillMaxHeight())
                Row(modifier = GlanceModifier.fillMaxWidth()) {
                    Spacer(GlanceModifier.width(0.dp).fillMaxWidth())
                    Box(
                        modifier = GlanceModifier
                            .size(18.dp)
                            .cornerRadius(999.dp)
                            .background(
                                ColorProvider(
                                    day = Color.White,
                                    night = Color(0xFF1E1E1E)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.spender_happy),
                            contentDescription = "앱 배지",
                            modifier = GlanceModifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}