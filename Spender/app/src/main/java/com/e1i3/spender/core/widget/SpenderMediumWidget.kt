package com.e1i3.spender.core.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
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
import com.e1i3.spender.MainActivity
import com.e1i3.spender.R
import com.e1i3.spender.core.data.service.getExpenseRate
import com.e1i3.spender.core.widget.component.BudgetProgressBarGlance
import com.e1i3.spender.core.widget.component.RefreshExpenseAction
import com.e1i3.spender.core.widget.component.WidgetButton
import com.e1i3.spender.ui.theme.DarkModeBackground
import com.e1i3.spender.ui.theme.LightPointColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpenderMediumWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            DpSize(120.dp, 120.dp),
            DpSize(213.dp, 213.dp),
            DpSize(240.dp, 240.dp)
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val percent0to100 = withContext(Dispatchers.IO) {
            runCatching { getExpenseRate() }.getOrElse { 0f }
        }
        val percent = (percent0to100 / 100f).coerceIn(0f, 1f)
        val percentText = "${percent0to100.toInt()}%"

        provideContent {
            SpenderMediumWidgetContent(
                percent = percent,
                percentText = percentText
            )
        }
    }
}

private fun deepLinkToHome(context: Context): Intent =
    Intent(context, MainActivity::class.java).apply {
        action = Intent.ACTION_VIEW
        data = "spender://home".toUri()
        addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        )
    }

private fun deepLinkToIncomeRegistration(context: Context): Intent =
    Intent(context, MainActivity::class.java).apply {
        action = Intent.ACTION_VIEW
        data = "spender://income_registration/1".toUri()
        addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        )
    }

private fun deepLinkToExpenseRegistration(context: Context): Intent =
    Intent(context, MainActivity::class.java).apply {
        action = Intent.ACTION_VIEW
        data = "spender://expense_registration/0".toUri()
        addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        )
    }

@Composable
fun SpenderMediumWidgetContent(
    percent: Float,
    percentText: String
) {
    val size = LocalSize.current
    val outerPad = 10.dp
    val side = (if (size.width < size.height) size.width else size.height) - (outerPad * 2)


    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = GlanceModifier
                .width(side)
                .height(side)
                .padding(horizontal = 13.dp, vertical = 9.dp)
                .background(ColorProvider(day = Color.White, night = DarkModeBackground))
                .clickable(onClick = actionStartActivity(deepLinkToHome(context = LocalContext.current))),
            contentAlignment = Alignment.TopStart
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
                        modifier = GlanceModifier.size(45.dp)
                    )

                    Text(
                        "지출이",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = ColorProvider(day = Color.Black, night = Color.White)
                        )
                    )

                    Spacer(GlanceModifier.width(55.dp))

                    Image(
                        provider = ImageProvider(android.R.drawable.ic_popup_sync),
                        contentDescription = "새로고침",
                        modifier = GlanceModifier
                            .size(20.dp)
                            .clickable(
                                onClick = actionRunCallback(
                                    RefreshExpenseAction::class.java,
                                    actionParametersOf(RefreshExpenseAction.KeyWidget to "medium")
                                )
                            ),
                        colorFilter = ColorFilter.tint(ColorProvider(LightPointColor))
                    )
                }

                Spacer(GlanceModifier.height(10.dp))

                BudgetProgressBarGlance(progress = percent, baseWidthDp = 160)

                Spacer(GlanceModifier.height(10.dp))

                Text(
                    percentText,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ColorProvider(day = Color.Black, night = Color.White)
                    )
                )

                Spacer(GlanceModifier.height(5.dp))

                Text(
                    text = "예산 대비 지출",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(day = LightPointColor, night = Color.White),
                        fontSize = 13.sp
                    )
                )

                Spacer(GlanceModifier.height(10.dp))

                Row(
                    modifier = GlanceModifier.fillMaxWidth().padding(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WidgetButton(
                        text = "수입",
                        onClickIntent = deepLinkToIncomeRegistration(context = LocalContext.current),
                        modifier = GlanceModifier
                            .defaultWeight()
                    )

                    Spacer(GlanceModifier.width(8.dp))

                    WidgetButton(
                        text = "지출",
                        onClickIntent = deepLinkToExpenseRegistration(context = LocalContext.current),
                        modifier = GlanceModifier
                            .defaultWeight()
                    )
                }
            }
        }
    }
}
