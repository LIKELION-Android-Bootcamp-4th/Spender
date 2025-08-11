package com.example.spender.feature.home.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spender.feature.home.domain.Notification
import com.example.spender.feature.home.domain.repository.NotificationListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(
    private val repository: NotificationListRepository
) : ViewModel() {

    // 로딩 상태
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // 알림 리스트
    private val _notifications = mutableStateOf<List<Notification>>(emptyList())
    val notifications: State<List<Notification>> = _notifications

    // 에러 메시지
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun load() {
        _isLoading.value = true
        _error.value = null

        repository.getNotificationList(
            onSuccess = { list ->
                _notifications.value = list
                _isLoading.value = false
            },
            onError = { e ->
                _error.value = e.message
                _isLoading.value = false
            }
        )
    }
}