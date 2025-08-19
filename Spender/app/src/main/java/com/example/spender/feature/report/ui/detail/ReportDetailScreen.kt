package com.example.spender.feature.report.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.core.common.util.toYearMonth
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.feature.report.mapper.toUiModel
import com.example.spender.feature.report.ui.component.BudgetProgressSection
import com.example.spender.feature.report.ui.component.CategorySpendingSection
import com.example.spender.feature.report.ui.component.EmotionTagSection
import com.example.spender.feature.report.ui.component.FeedbackSection
import com.example.spender.feature.report.ui.component.TotalSpendingSection

@Composable
fun ReportDetailScreen(
    navHostController: NavHostController,
    month: String,
    viewModel: ReportDetailViewModel = hiltViewModel(),

) {
    val report by viewModel.reportDetail

    LaunchedEffect (month){
        viewModel.loadReportDetail(month)
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "${report.month.toYearMonth()} 월간 리포트",
                navHostController,
                showBackButton = true
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                // 이번달 총 지출
                item {
                    TotalSpendingSection(totalExpense = report.totalExpense, totalBudget = report.totalBudget)
                }

                // 예산 대비 지출
                item {
                    BudgetProgressSection(totalExpense = report.totalExpense, totalBudget = report.totalBudget)
                }

                // 카테고리별 지출
                item {
                    CategorySpendingSection(report.byCategory.toUiModel())
                }

                // 감정 태그 비율
                item {
                    EmotionTagSection(report.byEmotion.toUiModel())
                }

                // 지출이의 의견
                item {
                    FeedbackSection(report.feedback)
                }

            }

        }

    )


}