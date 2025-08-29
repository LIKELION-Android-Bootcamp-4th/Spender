package com.e1i3.spender.core.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
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
import androidx.glance.appwidget.cornerRadius
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
import com.e1i3.spender.ui.theme.DarkModeDefaultFontColor
import com.e1i3.spender.ui.theme.LightPointColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpenderMediumWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            DpSize(160.dp, 130.dp),
            DpSize(180.dp, 130.dp),
            DpSize(180.dp, 180.dp),
            DpSize(240.dp, 190.dp),
            DpSize(240.dp, 200.dp),
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
        data = "spender://income_registration/0".toUri()
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

private data class UiScale(
    val outerPadding: Dp,
    val cardRadius: Dp,
    val innerHPad: Dp,
    val innerVPad: Dp,
    val logoSize: Dp,
    val refreshSize: Dp,
    val gapSmall: Dp,
    val gapMed: Dp,
    val buttonGap: Dp,
    val buttonHeight: Dp,
    val barWidthDp: Int,
    val titleSizeSp: Int,
    val percentSizeSp: Int,
    val captionSizeSp: Int,
)

@Composable
private fun rememberUiScale(): UiScale {
    val s = LocalSize.current
    val minSide = minOf(s.width, s.height).value
    val aspectRatio = s.width.value / s.height.value

    return when {
        aspectRatio > 1.2 -> UiScale(
            outerPadding = 10.dp, cardRadius = 16.dp,
            innerHPad = 13.dp, innerVPad = 9.dp,
            logoSize = 45.dp, refreshSize = 20.dp,
            gapSmall = 6.dp, gapMed = 8.dp, buttonGap = 8.dp, // gapMed 줄임
            buttonHeight = 38.dp,
            barWidthDp = (s.width - 30.dp).coerceIn(120.dp, 220.dp).value.toInt(),
            titleSizeSp = 14, percentSizeSp = 18, captionSizeSp = 13
        )
        minSide <= 140f -> UiScale(
            outerPadding = 8.dp, cardRadius = 14.dp,
            innerHPad = 10.dp, innerVPad = 8.dp,
            logoSize = 36.dp, refreshSize = 18.dp,
            gapSmall = 6.dp, gapMed = 8.dp, buttonGap = 6.dp,
            buttonHeight = 36.dp,
            barWidthDp = (s.width - 26.dp).coerceIn(100.dp, 200.dp).value.toInt(),
            titleSizeSp = 12, percentSizeSp = 16, captionSizeSp = 12
        )
        minSide <= 180f -> UiScale(
            outerPadding = 10.dp, cardRadius = 16.dp,
            innerHPad = 13.dp, innerVPad = 9.dp,
            logoSize = 45.dp, refreshSize = 20.dp,
            gapSmall = 8.dp, gapMed = 10.dp, buttonGap = 8.dp,
            buttonHeight = 40.dp,
            barWidthDp = (s.width - 30.dp).coerceIn(120.dp, 220.dp).value.toInt(),
            titleSizeSp = 14, percentSizeSp = 18, captionSizeSp = 13
        )
        else -> UiScale(
            outerPadding = 12.dp, cardRadius = 18.dp,
            innerHPad = 16.dp, innerVPad = 10.dp,
            logoSize = 50.dp, refreshSize = 22.dp,
            gapSmall = 10.dp, gapMed = 12.dp, buttonGap = 10.dp,
            buttonHeight = 44.dp,
            barWidthDp = (s.width - 36.dp).coerceIn(140.dp, 260.dp).value.toInt(),
            titleSizeSp = 15, percentSizeSp = 20, captionSizeSp = 14
        )
    }
}

@Composable
fun SpenderMediumWidgetContent(
    percent: Float,
    percentText: String
) {
    val ui = rememberUiScale()

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(ui.outerPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .cornerRadius(ui.cardRadius)
                .background(ColorProvider(day = Color.White, night = DarkModeBackground))
                .padding(horizontal = ui.innerHPad, vertical = ui.innerVPad)
                .clickable(onClick = actionStartActivity(deepLinkToHome(LocalContext.current))),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.spender_happy),
                        contentDescription = "로고",
                        modifier = GlanceModifier.size(ui.logoSize)
                    )

                    Text(
                        "지출이",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = ui.titleSizeSp.sp,
                            color = ColorProvider(day = Color.Black, night = DarkModeDefaultFontColor)
                        )
                    )

                    Spacer(GlanceModifier.defaultWeight())

                    Image(
                        provider = ImageProvider(android.R.drawable.ic_popup_sync),
                        contentDescription = "새로고침",
                        modifier = GlanceModifier
                            .size(ui.refreshSize)
                            .clickable(
                                onClick = actionRunCallback(
                                    RefreshExpenseAction::class.java,
                                    actionParametersOf(RefreshExpenseAction.KeyWidget to "medium")
                                )
                            ),
                        colorFilter = ColorFilter.tint(ColorProvider(LightPointColor))
                    )
                }

                Spacer(GlanceModifier.height(ui.gapMed))

                BudgetProgressBarGlance(progress = percent, baseWidthDp = ui.barWidthDp)

                Spacer(GlanceModifier.height(ui.gapMed))

                Text(
                    percentText,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = ui.percentSizeSp.sp,
                        color = ColorProvider(day = Color.Black, night = DarkModeDefaultFontColor)
                    )
                )

                Spacer(GlanceModifier.height(ui.gapSmall))

                Text(
                    text = "예산 대비 지출",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(day = LightPointColor, night = DarkModeDefaultFontColor),
                        fontSize = ui.captionSizeSp.sp
                    )
                )

                Spacer(GlanceModifier.height(ui.gapMed))

                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WidgetButton(
                        text = "수입",
                        onClickIntent = deepLinkToIncomeRegistration(LocalContext.current),
                        modifier = GlanceModifier
                            .defaultWeight()
                            .height(ui.buttonHeight)
                    )

                    Spacer(GlanceModifier.width(ui.buttonGap))

                    WidgetButton(
                        text = "지출",
                        onClickIntent = deepLinkToExpenseRegistration(LocalContext.current),
                        modifier = GlanceModifier
                            .defaultWeight()
                            .height(ui.buttonHeight)
                    )
                }
            }
        }
    }
}
