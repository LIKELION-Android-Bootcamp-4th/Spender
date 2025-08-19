package com.e1i3.spender.feature.mypage.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.e1i3.spender.core.data.service.getFirebaseAuth
import com.e1i3.spender.feature.expense.data.repository.RegularExpenseRepository
import com.e1i3.spender.feature.expense.domain.model.RegularExpense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegularExpenseViewModel @Inject constructor(
    private val repository: RegularExpenseRepository
) : ViewModel() {

    private val _regularExpenses = MutableStateFlow<List<RegularExpense>>(emptyList())
    val regularExpenses = _regularExpenses.asStateFlow()

    init {
        loadRegularExpenses()
    }

    private fun loadRegularExpenses() {
        val userId = getFirebaseAuth() ?: return
        repository.getRegularExpenses(userId) { expenses ->
            _regularExpenses.value = expenses
        }
    }
}