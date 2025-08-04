package com.example.spender.feature.mypage.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomTopAppBar

@Composable
fun RegularExpenseScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "정기지출 모아보기",
                navHostController,
                showBackButton = true
            )
        },
        content = { padding ->

            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                item {
                    Text("정기지출 모아보기")
                }
            }

        }
    )
}