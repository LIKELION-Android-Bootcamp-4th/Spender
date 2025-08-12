package com.example.spender.feature.mypage.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.spender.R
import com.example.spender.feature.mypage.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor() : ViewModel() {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        firebaseUser?.let { user ->
            val providerId = user.providerData.find { it.providerId != "firebase" }?.providerId

            val iconRes = when (providerId) {
                "google.com" -> R.drawable.google_icon
                "naver.com" -> R.drawable.naverlogo_icon
                "kakao.com" -> R.drawable.kakao_icon
                else -> null
            }

            val displayName = when (providerId) {
                "google.com" -> user.email ?: "사용자"
                else -> user.displayName ?: "사용자"
            }

            _user.value = User(
                displayName = displayName,
                providerIcon = iconRes
            )
        }
    }
}