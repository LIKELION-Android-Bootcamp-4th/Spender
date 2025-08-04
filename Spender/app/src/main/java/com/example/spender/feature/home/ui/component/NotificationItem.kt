package com.example.spender.feature.home.ui.component

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.feature.home.domain.Notification
import com.example.spender.feature.home.domain.NotificationType
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.Typography

@Composable
fun NotificationItem(notification: Notification) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ){
            val imageResId = when (notification.type) {
                NotificationType.BUDGET_ALERT -> R.drawable.notification_budget
                NotificationType.REPORT_ALERT -> R.drawable.notification_report
                NotificationType.REMINDER_ALERT -> R.drawable.notification_regular
            }

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "알림 이미지",
                modifier = Modifier.size(50.dp).padding(0.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = notification.title,
                        style = Typography.titleSmall
                    )

                    Text(
                        text = notification.date,
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
