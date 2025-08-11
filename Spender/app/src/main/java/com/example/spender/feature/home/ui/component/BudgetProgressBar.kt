package com.example.spender.feature.home.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.spender.ui.theme.LightSurface
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.PointRedColor

@Suppress("UnusedBoxWithConstraintsScope")
@Composable
fun BudgetProgressBar(
    percentage: Float,
    percentText: String
) {
    val highlightColor = if (percentage >= 1f) PointRedColor else PointColor

    Card(
        modifier = Modifier
            .height(108.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
        ) {
            BoxWithConstraints {
                val barWidth = maxWidth
                val bubbleOffset = (barWidth * percentage.coerceIn(0f, 1f)) - 24.dp

                // 말풍선
                Box(
                    modifier = Modifier
                        .offset(
                            x = bubbleOffset.coerceIn(0.dp, barWidth - 48.dp),
                            y = (-32).dp
                        )
                        .zIndex(1f)
                ) {
                    BubbleWithText(percentText, backgroundColor = highlightColor)
                }

                // 프로그래스 바
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .align(Alignment.CenterStart),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(RoundedCornerShape(100))
                            .background(Color(0xFFF0F0F0))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(percentage.coerceIn(0f, 1f))
                            .height(16.dp)
                            .clip(RoundedCornerShape(100))
                            .background(highlightColor)
                    )
                }
            }
        }
    }
}