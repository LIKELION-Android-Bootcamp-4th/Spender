package com.example.spender.feature.report.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.feature.report.ui.component.BudgetProgressSection
import com.example.spender.feature.report.ui.component.CategorySpendingSection
import com.example.spender.feature.report.ui.component.EmotionTagSection
import com.example.spender.feature.report.ui.component.FeedbackSection
import com.example.spender.feature.report.ui.component.TotalSpendingSection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(navHostController: NavHostController, reportId: Int) {


    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "${2025}년 1월 월간 리포트",  // TODO : 값 받아와서 해당 년도 & 월로 수정
                navHostController,
                showBackButton = true
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                // 이번달 총 지출
                item {
                    TotalSpendingSection()
                }

                // 예산 대비 지출
                item {
                    BudgetProgressSection()
                }

                // 카테고리별 지출
                item {
                    CategorySpendingSection()
                }

                // 감정 태그 비율
                item {
                    EmotionTagSection()
                }

                // 지출이의 의견
                item {
                    FeedbackSection()
                }

            }

        }

    )


}