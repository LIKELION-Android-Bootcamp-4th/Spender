package com.e1i3.spender.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.e1i3.spender.core.data.remote.expense.ExpenseDto
import com.e1i3.spender.feature.home.ui.component.RecentItem
import com.e1i3.spender.ui.theme.Typography
import com.e1i3.spender.ui.theme.navigation.BottomNavigationItem
import com.e1i3.spender.ui.theme.navigation.Screen

@Composable
fun RecentTransactionsSection(
    recentExpenses: List<ExpenseDto>,
    navHostController: NavHostController,
    bottomNavController: NavHostController
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier,
                    onClick = {
                        bottomNavController.navigate(BottomNavigationItem.Analysis.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                ) {
                    Text(
                        text = "캘린더 확인하기",
                        style = Typography.labelMedium.copy(
                            textDecoration = TextDecoration.Underline
                        ),
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        recentExpenses.forEach { expense ->
            RecentItem(
                title = expense.title,
                amount = expense.amount,
                type = "EXPENSE",
                date = expense.date.toDate(),
                onClick = {
                    navHostController.navigate(
                        Screen.ExpenseDetailScreen.createRoute(expense.id)
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}