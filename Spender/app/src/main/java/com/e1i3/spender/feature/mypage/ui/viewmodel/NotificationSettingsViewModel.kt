package com.e1i3.spender.feature.mypage.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.core.data.remote.notification.NotificationSettingsDto
import com.e1i3.spender.core.data.remote.notification.NotificationSettingsField
import com.e1i3.spender.feature.mypage.data.repository.NotificationSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val repository: NotificationSettingsRepository
) : ViewModel() {

    var settings by mutableStateOf<NotificationSettingsDto?>(null)
        private set

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // 최초 진입 시 기본값이 없다면 생성
    fun ensureDefaults(enabled: Boolean) {
        viewModelScope.launch {
            val result = repository.ensureDefaults(enabled)
            result.fold(
                onSuccess = {},
                onFailure = { e ->
                    _error.value = e.message
                }
            )
        }
    }

    // 현재 설정 로드
    fun load() {
        viewModelScope.launch {
            val result = repository.getNotificationSettings()
            result.fold(
                onSuccess = { dto -> settings = dto },
                onFailure = { e ->
                    _error.value = e.message
                }
            )

        }

    }

    // 개별 토글 업데이트
    fun toggleReport(enabled: Boolean) = update(NotificationSettingsField.Report, enabled)
    fun toggleBudget(enabled: Boolean) = update(NotificationSettingsField.Budget, enabled)
    fun toggleReminder(enabled: Boolean) = update(NotificationSettingsField.Reminder, enabled)

    private fun update(field: NotificationSettingsField, enabled: Boolean) {
        viewModelScope.launch {
            val result = repository.updateNotificationSetting(field = field, enabled = enabled)
            result.fold(
                onSuccess = {
                    settings = settings?.let {
                        when (field) {
                            NotificationSettingsField.Report -> it.copy(reportAlert = enabled)
                            NotificationSettingsField.Budget -> it.copy(budgetAlert = enabled)
                            NotificationSettingsField.Reminder -> it.copy(reminderAlert = enabled)
                        }
                    } ?: settings
                },
                onFailure = { e ->
                    _error.value = e.message
                }
            )
        }

    }
}