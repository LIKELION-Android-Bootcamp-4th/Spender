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
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.example.spender.feature.home.ui.component.RecentItem
import com.example.spender.ui.theme.Typography

@Composable
fun RecentTransactionsSection(recentExpenses: List<ExpenseDto>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "최근 지출 기록이에요!",
                style = Typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        recentExpenses.forEach { expense ->
            RecentItem(
                title = expense.title,
                amount = expense.amount
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}