package com.example.spender.feature.report.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.feature.report.ui.detail.component.BudgetProgressSection
import com.example.spender.feature.report.ui.detail.component.CategorySpendingSection
import com.example.spender.feature.report.ui.detail.component.EmotionTagSection
import com.example.spender.feature.report.ui.detail.component.FeedbackSection
import com.example.spender.feature.report.ui.detail.component.TotalSpendingSection
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(navHostController: NavHostController, reportId: Int) {


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "${2025}년 월간 리포트") // TODO : 값 받아와서 해당 년도로 수정
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 10.dp)
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