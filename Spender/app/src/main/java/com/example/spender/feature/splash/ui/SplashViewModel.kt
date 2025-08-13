package com.example.spender.feature.splash.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.feature.onboarding.data.OnboardingPref
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    sealed class SplashUiState {
        object Loading : SplashUiState()
        object NavigateToAuth : SplashUiState()
        object NavigateToMain : SplashUiState()
    }

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        checkLoginAndOnboarding()
    }

    private fun checkLoginAndOnboarding() = viewModelScope.launch {
        delay(1500) // 시간변경하셔도 됩니다...!!!

        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            _uiState.value = SplashUiState.NavigateToAuth
        } else {
            _uiState.value = SplashUiState.NavigateToMain
        }
    }
}