package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.e1i3.spender.core.data.remote.expense.ExpenseDto
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.feature.home.ui.BudgeProgress
import com.e1i3.spender.feature.home.ui.RecentTransactionsSection
import com.e1i3.spender.feature.home.ui.TotalExpenseCard

@Composable
fun HomeContent(
    friends: List<Friend>,
    tier: Int,
    totalExpense: Int,
    expenseRate: Float,
    recentExpenses: List<ExpenseDto>,
    onFriendClick: (Friend) -> Unit,
    onFriendDelete: (Friend) -> Unit,
    onAddFriendClick: () -> Unit,
    onTierClick: () -> Unit,
    navHostController: NavHostController,
    bottomNavController : NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
    ) {
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 10.dp)
            ) {
                item { AddFriendItem(onClick = onAddFriendClick) }
                items(friends) { friend ->
                    FriendItem(
                        friend = friend,
                        onClick = { onFriendClick(friend) },
                        onDeleteRequest = { onFriendDelete(friend) }
                    )
                }
            }
        }

        item {
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TierBadge(level = tier, onClick = onTierClick)
            }
        }

        item { TotalExpenseCard(totalExpense = totalExpense) }
        item { BudgeProgress(percentage = expenseRate, navHostController = navHostController) }
        item {
            RecentTransactionsSection(
                recentExpenses = recentExpenses,
                navHostController = navHostController,
                bottomNavController = bottomNavController
            )
        }
    }
}
