package com.e1i3.spender.feature.report.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.e1i3.spender.feature.home.ui.component.BudgetProgressBar
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.PointRedColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun BudgetProgressSection(
    totalExpense: Int,
    totalBudget: Int
) {
    val percentage = totalExpense.toFloat() / totalBudget.toFloat()
    val percentText = "${(percentage * 100).toInt()}%"
    val highlightColor = if (percentage >= 1f) PointRedColor else PointColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        Spacer(Modifier.height(24.dp))

        Text(
            buildAnnotatedString {
                append("예산의 ")
                withStyle(SpanStyle(highlightColor)) {
                    append((percentText))
                }
                append("를 썼어요")
            },
            style = Typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        BudgetProgressBar(percentage = percentage, percentText = percentText)

        Spacer(Modifier.height(8.dp))

    }
}