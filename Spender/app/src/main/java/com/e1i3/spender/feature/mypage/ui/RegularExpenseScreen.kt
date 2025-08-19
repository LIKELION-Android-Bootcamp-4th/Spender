package com.e1i3.spender.feature.mypage.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.feature.mypage.ui.component.RegularExpenseListItem
import com.e1i3.spender.feature.mypage.ui.viewmodel.RegularExpenseViewModel
import com.e1i3.spender.ui.theme.Typography
import com.e1i3.spender.ui.theme.navigation.Screen

@Composable
fun RegularExpenseScreen(
    navHostController: NavHostController,
    viewModel: RegularExpenseViewModel = hiltViewModel()
) {
    val regularExpenses by viewModel.regularExpenses.collectAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "정기지출 모아보기",
                navHostController,
                showBackButton = true,
                actions = {
                    IconButton(
                        onClick = {
                            navHostController.navigate(Screen.ExpenseRegistrationScreen.createRoute(2))
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "추가하기")
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                if (regularExpenses.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("등록된 정기 지출이 없습니다", style = Typography.bodyMedium)
                        }
                    }
                } else {
                    items(regularExpenses, key = { it.id }) { expense ->
                        RegularExpenseListItem(
                            regularExpense = expense,
                            onManageClick = {
                                navHostController.navigate(
                                    Screen.RegularExpenseDetailScreen.createRoute(expense.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}