package com.e1i3.spender.feature.home.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar

@Composable
fun FriendDetailScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "친구 지출 현황", // TODO: 친구 이름으로 변경?
                showBackButton = true,
                navController = navHostController,
                actions = {
                    // TODO : 친구 삭제
                }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {

            }
        }
    )
}