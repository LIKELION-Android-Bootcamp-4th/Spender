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
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

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
        getTotalExpense()
        getExpenseRate()
        getRecentExpenses()
        getFriendList()
        getCurrentTier()
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

    fun getFriendList() {
        viewModelScope.launch {
            repository.getFriendList()
                .onSuccess { list ->
                    _friendList.value = list
                }
        }
    }

    fun deleteFriend(friendId: String) {
        viewModelScope.launch {
            repository.deleteFriend(friendId)
        }
    }

    fun getCurrentTier() {
        viewModelScope.launch {
            repository.getCurrentTier()
                .onSuccess { _currentTier.value = it }
        }
    }

    fun getTotalExpense() {
        viewModelScope.launch {
            repository.getTotalExpense()
                .onSuccess { total ->
                    _totalExpense.value = total
                }
                .onFailure { e ->
                    e.printStackTrace()
                }
        }
    }

    fun getExpenseRate() {
        viewModelScope.launch {
            repository.getExpenseRate()
                .onSuccess { rate ->
                    _expenseRate.value = rate
                }
                .onFailure { e ->
                    e.printStackTrace()
                }
        }
    }

    fun getRecentExpenses() {
        viewModelScope.launch {
            repository.getExpenseListForHome()
                .onSuccess { expenses ->
                    _recentExpenses.value = expenses
                }
                .onFailure { e ->
                    e.printStackTrace()
                }
        }
    }
}