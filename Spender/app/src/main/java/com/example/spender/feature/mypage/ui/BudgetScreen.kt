package com.example.spender.feature.mypage.ui

import android.content.ClipData.Item
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomLongButton
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.feature.onboarding.ui.BudgetInputField
import com.example.spender.feature.onboarding.ui.OnboardingViewModel
import com.example.spender.ui.theme.Typography

@Composable
fun BudgetScreen(navHostController: NavHostController) {
    val viewModel: OnboardingViewModel = viewModel()
    val budget = 1000000 // TODO: 서버에서 가져온 값으로 교체

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "예산 설정",
                navHostController,
                showBackButton = true
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
//                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "한 달 예산을 입력해주세요",
                        style = Typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    BudgetInputField(
                        budget = budget,
                        onBudgetChange = { viewModel.onBudgetGet(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                CustomLongButton(
                    text = "설정",
                    onClick = {
                        // TODO : 예산 설정 로직
                    },
                    modifier = Modifier
                        .fillMaxWidth().height(56.dp),
                )
            }
        }
    )
}