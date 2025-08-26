package com.e1i3.spender.feature.home.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.feature.home.domain.repository.HomeRepository
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _hasUnread = mutableStateOf(false)
    val hasUnread: State<Boolean> = _hasUnread

    private val _currentTier = mutableStateOf(3)
    val currentTier: State<Int> = _currentTier

    private var listenerRegistered = false
    private var listenerRegistration: ListenerRegistration? = null

    private val _friendList = mutableStateOf<List<Friend>>(emptyList())
    val friendList: State<List<Friend>> = _friendList

    val totalExpense = repository.observeTotalExpense()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val expenseRate = repository.observeExpenseRate()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0f
        )

    val recentExpenses = repository.observeRecentExpenses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        checkUnreadNotifications()
        observeUnread()
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val friendListJob = async { repository.getFriendList() }
                val currentTierJob = async { repository.getCurrentTier() }

                friendListJob.await().onSuccess { _friendList.value = it }
                currentTierJob.await().onSuccess { _currentTier.value = it }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
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

    fun deleteFriend(friendId: String) {
        viewModelScope.launch {
            repository.deleteFriend(friendId)
            val updatedList = _friendList.value.filterNot { it.userId == friendId }
            _friendList.value = updatedList
        }
    }
}