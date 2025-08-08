package com.example.spender.feature.mypage.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.spender.core.data.remote.notification.NotificationSettingsDto
import com.example.spender.core.data.remote.notification.NotificationSettingsField
import com.example.spender.feature.mypage.data.repository.NotificationSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val repo: NotificationSettingsRepository
) : ViewModel() {

    var settings by mutableStateOf<NotificationSettingsDto?>(null)
        private set

    // 최초 진입 시 기본값이 없다면 생성
    fun ensureDefaults(enabled: Boolean) {
        repo.ensureDefaults(
            enabled = enabled,
            onSuccess = { load() },
            onError = {
            // TODO: 에러처리
            }
        )
    }

    // 현재 설정 로드
    fun load() {
        repo.getNotificationSettings(
            onSuccess = { dto -> settings = dto },
            onError = {
                // TODO: 에러처리
            }
        )
    }

    // 개별 토글 업데이트
    fun toggleReport(enabled: Boolean) = update(NotificationSettingsField.Report, enabled)
    fun toggleBudget(enabled: Boolean) = update(NotificationSettingsField.Budget, enabled)
    fun toggleReminder(enabled: Boolean) = update(NotificationSettingsField.Reminder, enabled)

    private fun update(field: NotificationSettingsField, enabled: Boolean) {
        repo.updateNotificationSetting(
            field = field,
            enabled = enabled,
            onSuccess = {
                settings = settings?.let {
                    when (field) {
                        NotificationSettingsField.Report -> it.copy(reportAlert = enabled)
                        NotificationSettingsField.Budget -> it.copy(budgetAlert = enabled)
                        NotificationSettingsField.Reminder -> it.copy(reminderAlert = enabled)
                    }
                } ?: settings
            },
            onError = {
                // TODO: 에러처리
            }
        )
    }
}