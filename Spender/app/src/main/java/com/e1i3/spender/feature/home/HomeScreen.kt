package com.e1i3.spender.feature.home

import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomDialog
import com.e1i3.spender.core.ui.LoadingScreen
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.feature.home.ui.component.HomeContent
import com.e1i3.spender.feature.home.ui.component.HomeTopBar
import com.e1i3.spender.feature.home.ui.component.HomeUiState
import com.e1i3.spender.feature.home.ui.viewModel.HomeViewModel
import com.e1i3.spender.ui.theme.navigation.Screen

@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.homeUiState.collectAsState()
    val hasUnread by viewModel.hasUnread
    var friendToDelete by remember { mutableStateOf<Friend?>(null) }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeTopBar(
                hasUnread = hasUnread,
                onSearchClick = { navHostController.navigate(Screen.SearchScreen.route) },
                onNotificationClick = { navHostController.navigate("notification_list") })
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        content = { padding ->

            when (uiState) {
                is HomeUiState.Loading -> LoadingScreen()
                is HomeUiState.Success -> {
                    val state = uiState as HomeUiState.Success
                    HomeContent(
                        friends = state.friends,
                        tier = state.tier,
                        totalExpense = state.totalExpense,
                        expenseRate = state.expenseRate,
                        recentExpenses = state.recentExpenses,
                        onFriendClick = { friend ->
                            navHostController.navigate(
                                Screen.FriendDetailScreen.createRoute(
                                    friend.userId
                                )
                            )
                        },
                        onFriendDelete = { friend -> friendToDelete = friend },
                        onAddFriendClick = { navHostController.navigate(Screen.FriendAddScreen.route) },
                        onTierClick = { navHostController.navigate("tier_history") },
                        navHostController = navHostController,
                        modifier = Modifier.padding(padding)
                    )
                }
            }

            if (friendToDelete != null) {
                CustomDialog(
                    title = "${friendToDelete!!.nickname}님을 삭제하시겠습니까?",
                    content = "삭제하더라도 나중에 다시 친구 추가가 가능해요.",
                    onConfirm = {
                        val deletedFriendName = friendToDelete!!.nickname
                        viewModel.deleteFriend(friendToDelete!!.userId)
                        Toast.makeText(
                            context,
                            "${deletedFriendName}님이 삭제되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        friendToDelete = null
                    },
                    onDismiss = {
                        friendToDelete = null
                    }
                )
            }

        }
    )
}