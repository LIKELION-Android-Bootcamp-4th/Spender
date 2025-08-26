package com.e1i3.spender.feature.mypage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.R
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.feature.mypage.ui.viewmodel.NotificationSettingsViewModel
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography

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
                        title = stringResource(R.string.notification_setting_report),
                        checked = settings?.reportAlert ?: false,
                        onCheckedChange = { viewModel.toggleReport(it) }
                    )
                }
                item {
                    NotificationSettingRow(
                        title = stringResource(R.string.notification_setting_regular),
                        checked = settings?.reminderAlert ?: false,
                        onCheckedChange = { viewModel.toggleReminder(it) }
                    )
                }
                item {
                    NotificationSettingRow(
                        title = stringResource(R.string.notification_setting_budget),
                        checked = settings?.budgetAlert ?: false,
                        onCheckedChange = { viewModel.toggleBudget(it) }
                    )
                }
                item {
                    NotificationSettingRow(
                        title = stringResource(R.string.notification_setting_deadline),
                        checked = settings?.reportDeadlineAlert ?: false,
                        onCheckedChange = { viewModel.toggleDeadline(it) }
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
            style = Typography.titleSmall,
            fontSize = 17.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.White, // 오프 동그라미 색
                uncheckedTrackColor = Color(0xFFD9D9D9), // 오프 배경 색
                uncheckedBorderColor = MaterialTheme.colorScheme.background,
                checkedThumbColor = Color.White,   // 온 동그라미 색
                checkedTrackColor = PointColor,   // 온 배경
                checkedBorderColor = MaterialTheme.colorScheme.background,
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