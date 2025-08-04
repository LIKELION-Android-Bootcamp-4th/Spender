package com.example.spender.feature.mypage.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navHostController: NavHostController) {
    var reportAlertEnabled by remember { mutableStateOf(false) }
    var recurringAlertEnabled by remember { mutableStateOf(false) }
    var budgetAlertEnabled by remember { mutableStateOf(false) }

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
                        checked = reportAlertEnabled,
                        onCheckedChange = { reportAlertEnabled = it }
                    )
                }
                item {
                    NotificationSettingRow(
                        title = "정기 지출 예정 알림",
                        checked = recurringAlertEnabled,
                        onCheckedChange = { recurringAlertEnabled = it }
                    )
                }
                item {
                    NotificationSettingRow(
                        title = "예산 초과/임박 알림",
                        checked = budgetAlertEnabled,
                        onCheckedChange = { budgetAlertEnabled = it }
                    )
                }
            }        }    )
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
            onCheckedChange = onCheckedChange
        )
    }
}