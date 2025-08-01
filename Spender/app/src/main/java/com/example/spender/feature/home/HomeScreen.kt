package com.example.spender.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.feature.home.ui.BudgeProgress
import com.example.spender.feature.home.ui.RecentTransactionsSection
import com.example.spender.feature.home.ui.TotalExpenseCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val totalExpense = 542560
    val budget = 1000000

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(56.dp),
                title = { },
                actions = {
                    IconButton(
                        onClick = {
                            // TODO: 알림 모아보기로 이동하는 로직
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "알림 보기",
                        )
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                item {
                    TotalExpenseCard(totalExpense = totalExpense)
                }
                item {
                    BudgeProgress(budget = budget, totalExpense = totalExpense)
                }
                item {
                    RecentTransactionsSection()
                }
            }
        }
    )
}