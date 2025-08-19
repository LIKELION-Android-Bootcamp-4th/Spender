package com.example.spender.feature.home.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.feature.home.domain.repository.HomeRepository
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
): ViewModel() {

    private val _hasUnread = mutableStateOf(false)
    val hasUnread: State<Boolean> = _hasUnread

    private var listenerRegistered = false
    private var listenerRegistration: ListenerRegistration? = null

    init {
        checkUnreadNotifications()
        observeUnread()
    }

    fun checkUnreadNotifications() {
        viewModelScope.launch {
            repository.hasUnreadNotifications()
                .onSuccess { result ->
                    _hasUnread.value = result
                }
                .onFailure { e ->
                    e.printStackTrace()
                }
        }
    }

    private fun observeUnread() {
        if (listenerRegistered) return
        listenerRegistered = true

        listenerRegistration = repository.observeUnreadNotifications { hasUnread ->
            _hasUnread.value = hasUnread
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }

}