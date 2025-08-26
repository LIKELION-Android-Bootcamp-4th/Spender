package com.e1i3.spender.core.data.remote.notification

data class NotificationSettingsDto(
    val budgetAlert: Boolean = false,
    val reportAlert: Boolean = false,
    val reminderAlert: Boolean = false,
    val reportDeadlineAlert: Boolean = false
)

enum class NotificationSettingsField(val path: String){
    Budget("notificationSettings.budgetAlert"),
    Report("notificationSettings.reportAlert"),
    Reminder("notificationSettings.reminderAlert"),
    ReportDeadline("notificationSettings.reportDeadlineAlert")
}