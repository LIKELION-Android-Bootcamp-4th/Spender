package com.example.spender.feature.expense.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.ui.theme.NotoSansFamily
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.navigation.Screen
import com.google.common.math.LinearTransformation.horizontal
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseRegistrationParentScreen(
    navHostController: NavHostController,
    initialTabIndex: Int,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val tabs = listOf("영수증", "지출", "정기지출")

    LaunchedEffect(key1 = initialTabIndex) {
        viewModel.setInitialTabIndex(initialTabIndex)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is RegistrationEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is RegistrationEvent.OcrSuccess -> {
                    val encodedTitle =
                        URLEncoder.encode(event.title, StandardCharsets.UTF_8.toString())
                    val encodedAmount =
                        URLEncoder.encode(event.amount, StandardCharsets.UTF_8.toString())
                    val encodedDate =
                        URLEncoder.encode(event.date, StandardCharsets.UTF_8.toString())

                    navHostController.navigate(
                        Screen.OcrResultScreen.createRoute(encodedTitle, encodedAmount, encodedDate)
                    )
                }

                is RegistrationEvent.NavigateBack -> {
                    navHostController.popBackStack() //이전화면으로 돌아가기
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "지출 등록",
                navHostController,
                showBackButton = true,
            )
        },
        bottomBar = {
            if (uiState.selectedTabIndex == 1 || uiState.selectedTabIndex == 2) {
                Button(
                    onClick = { viewModel.onRegisterClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        "지출 등록",
                        modifier = Modifier.padding(vertical = 6.dp),
                        style = Typography.titleSmall
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // 탭 메뉴
            TabRow(
                selectedTabIndex = uiState.selectedTabIndex,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(10.dp)),
                containerColor = MaterialTheme.colorScheme.tertiary,
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[uiState.selectedTabIndex])
                            .fillMaxSize()
                            .padding(4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = uiState.selectedTabIndex == index
                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .fillMaxHeight()
                            .zIndex(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { viewModel.onTabSelected(index) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = Typography.titleMedium,
                            color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
            when (uiState.selectedTabIndex) {
                0 -> OcrContent(uiState, viewModel)
                1 -> ExpenseContent(
                    uiState,
                    viewModel,
                    onManageCategoriesClick = {
                        navHostController.navigate(Screen.ExpenseCategoryScreen.route)
                    }
                )

                2 -> RecurringExpenseContent(
                    uiState,
                    viewModel,
                    onManageCategoriesClick = {
                        navHostController.navigate(Screen.ExpenseCategoryScreen.route)
                    }
                )
            }
        }
    }
}