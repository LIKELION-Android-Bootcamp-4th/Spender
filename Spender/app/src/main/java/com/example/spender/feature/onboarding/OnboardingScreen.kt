package com.example.spender.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spender.feature.onboarding.ui.OnboardingViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.R
import com.example.spender.core.ui.CustomLongButton
import com.example.spender.feature.onboarding.data.OnboardingPref
import com.example.spender.feature.onboarding.ui.BudgetInputField
import com.example.spender.feature.onboarding.ui.PageIndicator
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.navigation.Screen

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    viewModel: OnboardingViewModel = viewModel()
) {
    val context = LocalContext.current

    val currentPage by viewModel.currentPage.collectAsState()
    val budget by viewModel.budget.collectAsState()
    val isBudgetValid = viewModel.isBudgetValid

    val titles = context.resources.getStringArray(R.array.onboarding_title).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageIndicator(currentPage = currentPage, pageCount = titles.size)

            Text(
                text = titles[currentPage],
                style = Typography.titleLarge,
                textAlign = TextAlign.Center
            )

            if (currentPage == 1) {
                Spacer(modifier = Modifier.height(80.dp))
                BudgetInputField(
                    budget = budget,
                    onBudgetChange = { viewModel.onBudgetGet(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        CustomLongButton(
            text = if (currentPage < 2) "다음" else "시작하기",
            onClick = {
                if (currentPage < 2) {
                    viewModel.onNext()
                } else {
                    OnboardingPref.setShown(context)
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.OnboardingScreen.route) {
                            inclusive = true
                        }
                    }
                }
            },
            isEnabled = currentPage != 1 || isBudgetValid
        )
    }
}