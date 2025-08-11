package com.example.spender.feature.home.domain

import java.util.Date

data class Notification(
    val id: String,
    val title: String,
    val content: String,
    val date: Date,
    val type: NotificationType
)

enum class NotificationType {
    BUDGET_ALERT,
    REPORT_ALERT,
    REMINDER_ALERT
}