package com.example.spender.feature.onboarding.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel : ViewModel() {
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _budget = MutableStateFlow(0)
    val budget: StateFlow<Int> = _budget.asStateFlow()

    fun onNext() {
        _currentPage.value += 1
    }

    fun onBudgetGet(budget: Int) {
        _budget.value = budget
        Log.d("Budget", "onBudgetGet: ${_budget.value}")
    }

    val isBudgetValid: Boolean
        get() = _budget.value > 0
}