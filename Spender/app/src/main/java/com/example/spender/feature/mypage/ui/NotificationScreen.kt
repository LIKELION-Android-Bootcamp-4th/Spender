package com.example.spender.feature.mypage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.feature.mypage.ui.viewmodel.NotificationSettingsViewModel
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navHostController: NavHostController,
    viewModel: NotificationSettingsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    val settings = viewModel.settings

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "알림 설정",
                navHostController,
                showBackButton = true
            )
        },
        content = { padding ->

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 34.dp)
            ) {
                item {
                    NotificationSettingRow(
                        title = "지출 리포트 피드백 알림",
                        checked = settings?.reportAlert ?: false,
                        onCheckedChange = { viewModel.toggleReport(it) }
                    )
                }
                item {
                    NotificationSettingRow(
                        title = "정기 지출 예정 알림",
                        checked = settings?.reminderAlert ?: false,
                        onCheckedChange = { viewModel.toggleReminder(it) }
                    )
                }
                item {
                    NotificationSettingRow(
                        title = "예산 초과/임박 알림",
                        checked = settings?.budgetAlert ?: false,
                        onCheckedChange = { viewModel.toggleBudget(it) }
                    )
                }
            }
        })
}

@Composable
private fun NotificationSettingRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = Typography.titleMedium
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.White, // 오프 동그라미 색
                uncheckedTrackColor = Color(0xFFD9D9D9), // 오프 배경 색
                uncheckedBorderColor = Color.White,
                checkedThumbColor = Color.White,   // 온 동그라미 색
                checkedTrackColor = PointColor,   // 온 배경
                checkedBorderColor = Color.White,
            ),
            modifier = Modifier.scale(1.2f),
            thumbContent = {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            color = if (checked) Color.White else Color.White,
                            shape = CircleShape
                        )
                )
            },


            )
    }
}