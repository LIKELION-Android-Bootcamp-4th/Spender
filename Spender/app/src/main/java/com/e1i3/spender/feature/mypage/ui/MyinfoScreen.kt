package com.e1i3.spender.feature.mypage.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar

@Composable
fun MyinfoScreen(
    navHostController: NavHostController) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "내 정보",
                navHostController,
                showBackButton = true
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileSection()
            }
        }
    )
}
