package com.e1i3.spender.feature.home.mapper

import com.e1i3.spender.core.data.remote.notification.NotificationDto
import com.e1i3.spender.feature.home.domain.model.Notification
import com.e1i3.spender.feature.home.domain.model.NotificationType
import com.google.firebase.Timestamp
import java.util.Date

fun NotificationDto.toDomain(id: String): Notification? {
    val date: Date = (createdAt ?: Timestamp.now()).toDate()
    val typeEnum = when (type.uppercase()) {
        "BUDGET_ALERT" -> NotificationType.BUDGET_ALERT
        "REMINDER_ALERT" -> NotificationType.REMINDER_ALERT
        "REPORT_ALERT" -> NotificationType.REPORT_ALERT
        else -> return null
    }
    return Notification(
        id = id,
        title = title,
        content = content,
        date = date,
        type = typeEnum,
        route = route,
        isRead = isRead
    )
}