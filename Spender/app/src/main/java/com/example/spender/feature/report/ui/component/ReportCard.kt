package com.example.spender.feature.report.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spender.core.common.util.formatToManWon
import com.example.spender.feature.report.domain.model.Report
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

@Composable
fun ReportSummaryCard(
    report: Report,
    onClick: () -> Unit
) {
    val usageRatio = if (report.totalBudget > 0) {
        report.totalExpense.toFloat() / report.totalBudget
    } else 0f
    val usagePercent = (usageRatio * 100).toInt()
    val isOverBudget = usageRatio > 1f
    val progressColor = if (isOverBudget) Color.Red else PointColor

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surface)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(12.dp),

        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = report.month, fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isOverBudget) "+${usagePercent - 100}%" else "-${100 - usagePercent}%",
                color = progressColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { usageRatio.coerceIn(0f, 1.5f) },
                color = progressColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${formatToManWon(report.totalExpense)} / ${formatToManWon(report.totalBudget)}",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ReportSummaryCardHorizontal(
    report: Report,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isHighlighted: Boolean = false
) {
    val usageRatio = if (report.totalBudget > 0) {
        report.totalExpense.toFloat() / report.totalBudget
    } else 0f

    val usagePercent = (usageRatio * 100).toInt()
    val isOverBudget = usageRatio > 1f
    val percentDiff = if (isOverBudget) usagePercent - 100 else 100 - usagePercent
    val percentText = if (isOverBudget) "+$percentDiff%" else "-$percentDiff%"
    val percentColor = if (isOverBudget) Color.Red else PointColor

//    val backgroundColor = if (isHighlighted) Color.White else Color.White
//    val borderStroke = if (isHighlighted) BorderStroke(0.dp, LightPointColor) else null
//    val elevation = if (isHighlighted) 60.dp else 2.dp

    val backgroundColor by animateColorAsState(
        if (isHighlighted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    )
    // CFE0F9
    // D8E6FB

    val border = if (isHighlighted) BorderStroke(1.5.dp, Color(0xFF000000)) else null

    val elevation = if (isHighlighted) 8.dp else 2.dp


    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() }
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(backgroundColor, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(elevation),
//        border = border
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 좌측: 년월
            Text(
                text = report.month,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                style = Typography.titleLarge
            )

            // 가운데: 총 지출
            Text(
                text = formatToManWon(report.totalExpense),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 우측: 예산 대비 퍼센트
            Text(
                text = percentText,
                color = percentColor,
                style = Typography.bodyMedium
            )
        }
    }
}

