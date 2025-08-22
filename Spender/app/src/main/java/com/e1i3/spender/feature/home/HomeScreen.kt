package com.e1i3.spender.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.R
import com.e1i3.spender.core.data.remote.expense.ExpenseDto
import com.e1i3.spender.core.data.service.getExpenseListForHome
import com.e1i3.spender.core.data.service.getExpenseRate
import com.e1i3.spender.core.data.service.getTotalExpense
import com.e1i3.spender.feature.home.ui.BudgeProgress
import com.e1i3.spender.feature.home.ui.RecentTransactionsSection
import com.e1i3.spender.feature.home.ui.TotalExpenseCard
import com.e1i3.spender.feature.home.ui.component.FriendItem
import com.e1i3.spender.feature.home.ui.component.TierBadge
import com.e1i3.spender.feature.home.ui.viewModel.HomeViewModel
import com.e1i3.spender.ui.theme.LightPointColor
import com.e1i3.spender.ui.theme.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    var totalExpense by remember { mutableIntStateOf(0) }
    var percentage by remember { mutableFloatStateOf(0f) }
    var recentExpenses by remember { mutableStateOf<List<ExpenseDto>>(emptyList()) }
    val hasUnread by viewModel.hasUnread
    val friendList by viewModel.friendList
    val currentTier = viewModel.currentTier.value

    LaunchedEffect(Unit) {
        viewModel.getFriendList()
        viewModel.getCurrentTier()
    }

    LaunchedEffect(hasUnread) {
        totalExpense = getTotalExpense()
        percentage = getExpenseRate()
        recentExpenses = getExpenseListForHome()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ci_temp),
                        contentDescription = "프로필 이미지",
                        modifier = Modifier.size(100.dp)
                    )
                },
                title = { },
                actions = {
                    IconButton(
                        onClick = {
                            navHostController.navigate(Screen.SearchScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색",
                            modifier = Modifier.size(28.dp),
                            tint = LightPointColor
                        )
                    }

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
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(friendList) { friend ->
                            FriendItem(
                                navHostController = navHostController,
                                friend = friend,
                                viewModel = viewModel
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TierBadge(level = currentTier, onClick = {
                            navHostController.navigate("tier_history")
                        })
                    }
                }

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