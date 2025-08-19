package com.e1i3.spender.feature.home.domain

import java.util.Date

data class Notification(
    val id: String,
    val title: String,
    val content: String,
    val date: Date,
    val type: NotificationType,
    val route: String,
    val isRead: Boolean
)

enum class NotificationType {
    BUDGET_ALERT,
    REPORT_ALERT,
    REMINDER_ALERT
}