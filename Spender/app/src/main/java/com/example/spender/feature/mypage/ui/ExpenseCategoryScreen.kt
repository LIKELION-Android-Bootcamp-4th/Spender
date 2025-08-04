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
fun ExpenseCategoryScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "지출 카테고리 설정",
                navHostController,
                showBackButton = true
            )
        },
        content = { padding ->

            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                item {
                    Text("지출 카테고리 설정")
                }
            }

        }
    )
}