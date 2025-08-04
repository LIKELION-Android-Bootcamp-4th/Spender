package com.example.spender.feature.home.domain

data class Notification(
    val id: Int,
    val title: String,
    val content: String,
    val date: String,
    val isRead: Boolean,
    val type: NotificationType
)

enum class NotificationType {
    BUDGET_ALERT,
    REPORT_ALERT,
    REMINDER_ALERT
}