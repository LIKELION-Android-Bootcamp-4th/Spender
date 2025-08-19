package com.example.spender.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.example.spender.core.data.service.getExpenseListForHome
import com.example.spender.core.data.service.getExpenseRate
import com.example.spender.core.data.service.getTotalExpense
import com.example.spender.feature.home.ui.BudgeProgress
import com.example.spender.feature.home.ui.HomeViewModel
import com.example.spender.feature.home.ui.RecentTransactionsSection
import com.example.spender.feature.home.ui.TotalExpenseCard
import com.example.spender.ui.theme.LightPointColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    var totalExpense by remember { mutableStateOf(0) }
    var percentage by remember { mutableStateOf(0f) }
    var recentExpenses by remember { mutableStateOf<List<ExpenseDto>>(emptyList()) }
    val hasUnread by viewModel.hasUnread

    LaunchedEffect(hasUnread) {
        totalExpense = getTotalExpense()
        percentage = getExpenseRate()
        recentExpenses = getExpenseListForHome()
    }

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
                        Box(contentAlignment = Alignment.TopEnd) {
                            Icon(
                                imageVector = Icons.Rounded.Notifications,
                                contentDescription = "알림",
                                modifier = Modifier.size(28.dp),
                                tint = LightPointColor
                            )
                            if (hasUnread) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .offset(x = 2.dp, y = 0.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
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
                        percentage = percentage,
                        navHostController = navHostController
                    )
                }
                item {
                    RecentTransactionsSection(
                        recentExpenses = recentExpenses,
                        navHostController = navHostController
                    )
                }
            }
        }
    )
}