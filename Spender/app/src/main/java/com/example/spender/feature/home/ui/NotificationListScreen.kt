package com.example.spender.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.feature.home.domain.Notification
import com.example.spender.feature.home.domain.NotificationType
import com.example.spender.feature.home.ui.component.NotificationItem
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.Typography

@Composable
fun NotificationListScreen(navHostController: NavHostController){
    val dummyNotifications = listOf(
        Notification(
            id = 1,
            title = "예산 초과 임박 알림",
            content = "이번 달 예산의 90%를 사용했어요. 지출을 줄여보세요!",
            date = "8월 4일",
            isRead = false,
            type = NotificationType.BUDGET_ALERT
        ),
        Notification(
            id = 2,
            title = "정기 지출 알림",
            content = "오늘 넷플릭스 17,000원이 지출로 기록되었어요.",
            date = "8월 3일",
            isRead = true,
            type = NotificationType.REMINDER_ALERT
        ),
        Notification(
            id = 3,
            title = "보고서 생성 완료",
            content = "7월 지출 리포트가 생성되었습니다. 확인해보세요!",
            date = "8월 1일",
            isRead = false,
            type = NotificationType.REPORT_ALERT
        ),
        Notification(
            id = 3,
            title = "정기 지출 알림",
            content = "오늘 월세 100만원이 지출로 기록되었어요.",
            date = "7월 31일",
            isRead = true,
            type = NotificationType.REMINDER_ALERT
        ),
        Notification(
            id = 4,
            title = "정기 지출 알림",
            content = "오늘 핸드폰 요금 5만원이 지출로 기록되었어요.",
            date = "7월 29일",
            isRead = true,
            type = NotificationType.REMINDER_ALERT
        ),
    )

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "알림",
                showBackButton = true,
                navController = navHostController,
                actions = {
                    TextButton(
                        onClick = {
                            navHostController.navigate("notification")
                        }
                    ) {
                        Text(text = "설정", style = Typography.bodyMedium)
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(dummyNotifications){ notification ->
                    NotificationItem(notification = notification)
                }

                // TODO: 해당 알림 클릭했을때 이동할까 말까
                item {
                   Text(
                        text = "7일 전 알림까지 확인할 수 있어요.",
                        style = Typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        textAlign = TextAlign.Center,
                       color = LightFontColor
                    )
                }
            }

        }
    )
}