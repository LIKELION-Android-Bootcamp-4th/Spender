package com.example.spender.feature.onboarding.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.spender.feature.onboarding.domain.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: OnboardingRepository
) : ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    var budget by mutableStateOf(0)
        private set

    fun onNext() {
        _currentPage.value += 1
    }

    fun loadBudget() {
        repository.getUserBudget { amount ->
            budget = amount ?: 0
        }
    }

    fun updateBudget(newBudget: Int) {
        budget = newBudget
    }

    fun saveBudget(onComplete: (Boolean) -> Unit) {
        repository.setUserBudget(budget) { success ->
            onComplete(success)
        }
    }
}