package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.e1i3.spender.R
import com.e1i3.spender.feature.home.domain.model.Notification
import com.e1i3.spender.feature.home.domain.model.NotificationType
import com.e1i3.spender.ui.theme.LightFontColor
import com.e1i3.spender.ui.theme.LightReportHighlightColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun NotificationItem(
    notification: Notification,
    rootNavController: NavHostController,
    dateText: String
) {
    val backgroundColor = if (!notification.isRead) LightReportHighlightColor else Color.White
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 15.dp, horizontal = 20.dp)
            .clickable {
                when {
                    notification.route.startsWith("report_detail/") -> {
                        rootNavController.navigate(
                            notification.route
                        )
                    }
//                    notification.route == "analysis" -> {
//                        rootNavController.navigate(
//                            notification.route
//                        )
//                    }
//
//                    notification.route == "home" -> {
//                        rootNavController.navigate(
//                            notification.route
//                        )
//                    }

                    else -> {
//                        rootNavController.navigate(
//                            com.e1i3.spender.ui.theme.navigation.BottomNavigationItem.Home.route
//                        )
                    }
                }
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageResId = when (notification.type) {
                NotificationType.BUDGET_ALERT -> R.drawable.notification_budget
                NotificationType.REPORT_ALERT -> R.drawable.notification_report
                NotificationType.REMINDER_ALERT -> R.drawable.notification_regular
                NotificationType.REPORT_DEADLINE_ALERT -> R.drawable.notification_report_deadline
            }

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "알림 이미지",
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = notification.title,
                        style = Typography.titleSmall,
                        modifier = Modifier.weight(1f),
                        softWrap = true
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = dateText,
                        style = Typography.labelMedium,
                        color = LightFontColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = notification.content,
                    style = Typography.bodyMedium
                )
            }
        }
    }
}