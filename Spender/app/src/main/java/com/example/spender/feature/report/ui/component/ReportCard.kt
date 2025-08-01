package com.example.spender.feature.report.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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

@Composable
fun ReportSummaryCard(
    report: Report,
    onClick: () -> Unit
) {
    val usageRatio = if (report.budget > 0) {
        report.totalExpense.toFloat() / report.budget
    } else 0f
    val usagePercent = (usageRatio * 100).toInt()
    val isOverBudget = usageRatio > 1f
    val progressColor = if (isOverBudget) androidx.compose.ui.graphics.Color.Red else PointColor

    Card( // TODO : ui 수정
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
            .background(Color.White)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(12.dp),

        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = report.yearMonth, fontWeight = FontWeight.Bold, fontSize = 20.sp)

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
                text = "${formatToManWon(report.totalExpense)} / ${formatToManWon(report.budget)}",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp
            )
        }
    }
}

