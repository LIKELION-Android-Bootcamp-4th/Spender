package com.e1i3.spender.feature.home.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.core.data.remote.expense.ExpenseDto
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.feature.home.domain.repository.HomeRepository
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
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

    private val _totalExpense = mutableStateOf(0)
    val totalExpense: State<Int> = _totalExpense

    private val _expenseRate = mutableStateOf(0f)
    val expenseRate: State<Float> = _expenseRate

    private val _recentExpenses = mutableStateOf<List<ExpenseDto>>(emptyList())
    val recentExpenses: State<List<ExpenseDto>> = _recentExpenses

    init {
        checkUnreadNotifications()
        observeUnread()
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val totalExpenseJob = async { repository.getTotalExpense() }
                val expenseRateJob = async { repository.getExpenseRate() }
                val recentExpensesJob = async { repository.getExpenseListForHome() }
                val friendListJob = async { repository.getFriendList() }
                val currentTierJob = async { repository.getCurrentTier() }

                totalExpenseJob.await().onSuccess { _totalExpense.value = it }
                expenseRateJob.await().onSuccess { _expenseRate.value = it }
                recentExpensesJob.await().onSuccess { _recentExpenses.value = it }
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