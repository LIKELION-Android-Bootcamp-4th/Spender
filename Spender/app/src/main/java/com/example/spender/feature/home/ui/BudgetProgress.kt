package com.example.spender.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

@Composable
fun BudgeProgress(budget: Int, totalExpense: Int) {
    val percentText = "${((totalExpense.toFloat() / budget.toFloat()) * 100).toInt()}%"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "지금까지 예산의 ",
                style = Typography.titleMedium
            )
            Text(
                text = "$percentText",
                style = Typography.titleMedium.copy(
                    color = PointColor
                )
            )
            Text(
                text = "를 썼어요!",
                style = Typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        BudgetProgressBar(budget = budget, totalExpense = totalExpense)

    }
}