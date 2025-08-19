package com.e1i3.spender.feature.mypage.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.e1i3.spender.R
import com.e1i3.spender.feature.mypage.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        if (firebaseUser == null) {
            return
        }

        FirebaseFirestore.getInstance().collection("users").document(firebaseUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val provider = document.getString("provider")
                    val name = document.getString("name")
                    val email = document.getString("email")
                    val nickname = document.getString("nickname")

                    val iconRes = when (provider) {
                        "google" -> R.drawable.google_icon
                        "naver" -> R.drawable.naverlogo_icon
                        "kakao" -> R.drawable.kakao_icon
                        else -> null
                    }

                    val displayName = when (provider) {
                        "google" -> email ?: "사용자"
                        else -> name ?: "사용자"
                    }

                    val displayEmail = when (provider) {
                        "kakao" -> email ?: "사용자"
                        else -> email ?: "사용자"
                    }

                    val displayNickname = when (provider) {
                        "kakao" -> nickname ?: "사용자"
                        else -> nickname ?: "사용자"
                    }

                    _user.value = User(
                        displayName = displayName,
                        displayEmail = displayEmail,
                        providerIcon = iconRes,
                        displayNickname = displayNickname
                    )
                }
            }
            .addOnFailureListener {
                val displayName = firebaseUser.displayName ?: firebaseUser.email ?: "사용자"
                _user.value = User(displayName = displayName)
            }
    }
}