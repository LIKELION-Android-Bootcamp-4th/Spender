package com.e1i3.spender.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.core.ui.LoadingScreen
import com.e1i3.spender.feature.home.ui.component.TierBadge
import com.e1i3.spender.feature.home.ui.viewModel.FriendDetailViewModel

@Composable
fun FriendDetailScreen(
    navHostController: NavHostController,
    friendId: String,
    viewModel: FriendDetailViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val friend = viewModel.friendDetail.value

    LaunchedEffect(friendId) {
        viewModel.loadFriendDetail(friendId)
    }

    when {
        isLoading -> LoadingScreen()
        friend == null -> {
            Text("친구 정보를 찾을 수 없습니다.")
        }
        else -> {
            Scaffold(
                topBar = {
                    CustomTopAppBar(
                        title = "${friend.nickname} 지출 현황",
                        showBackButton = true,
                        navController = navHostController,
                        actions = {
                            // TODO : 친구 삭제
                        }
                    )
                },
                content = { padding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(padding)
                            .padding(vertical = 10.dp, horizontal = 8.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TierBadge(level = 1)
                            }
                        }

                        item {
                            FriendBudgetProgressSection(friend = friend, navHostController = navHostController
                            )
                        }

                        item {
                            FriendCategorySection(friend = friend)
                        }

                        item {
                            FriendEmotionSection(friend = friend)
                        }
                    }
                }
            )
        }
    }
}

