package com.e1i3.spender.feature.mypage.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomLongButton
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.feature.onboarding.ui.BudgetInputField
import com.e1i3.spender.feature.onboarding.ui.OnboardingViewModel
import com.e1i3.spender.ui.theme.Typography

@Composable
fun BudgetScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val viewModel: OnboardingViewModel = hiltViewModel()
    val budget = viewModel.budget

    LaunchedEffect(Unit) {
        viewModel.loadBudget()
    }

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
                        onBudgetChange = { viewModel.updateBudget(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                CustomLongButton(
                    text = "설정",
                    onClick = {
                        viewModel.saveBudget { success ->
                            if (success) {
                                navHostController.popBackStack()
                            } else {
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                )
            }
        }
    )
}