package com.example.spender.feature.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.feature.home.ui.BudgeProgress
import com.example.spender.feature.home.ui.RecentTransactionsSection
import com.example.spender.feature.home.ui.TotalExpenseCard
import com.example.spender.ui.theme.LightPointColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val totalExpense = 542560
    val budget = 1000000

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                actions = {
                    IconButton(
                        onClick = {
                            navHostController.navigate("notification_list")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = "알림 보기",
                            modifier = Modifier.size(28.dp),
                            tint = LightPointColor
                        )
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        },

        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 5.dp),
            ) {
                item {
                    TotalExpenseCard(totalExpense = totalExpense)
                }
                item {
                    BudgeProgress(
                        budget = budget,
                        totalExpense = totalExpense,
                        navHostController = navHostController
                    )
                }
                item {
                    RecentTransactionsSection() // TODO: 각 데이터의 필드(수입,지출 제목 & 금액) 넘겨줘야 함
                }
            }
        }
    )
}