package com.e1i3.spender.feature.mypage.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.R
import com.e1i3.spender.feature.mypage.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor() : ViewModel() {

    private val _user = MutableStateFlow(User(displayNickname = "사용자"))
    val user = _user.asStateFlow()

    private val _updateNicknameState =
        MutableStateFlow<UpdateNicknameState>(UpdateNicknameState.Idle)
    val updateNicknameState = _updateNicknameState.asStateFlow()

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
                _user.value = User(
                    displayName = displayName,
                    displayNickname = displayName
                )
            }

    }

    fun updateNickname(newNickname: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            _updateNicknameState.value = UpdateNicknameState.Error("로그인이 필요합니다.")
            resetUpdateNicknameState()
            return
        }

        if (newNickname.isBlank()) {
            _updateNicknameState.value = UpdateNicknameState.Error("닉네임을 입력해주세요.")
            resetUpdateNicknameState()
            return
        }

        if (newNickname.length > 20) {
            _updateNicknameState.value = UpdateNicknameState.Error("닉네임은 20자 이하로 입력해주세요.")
            resetUpdateNicknameState()
            return
        }

        if (newNickname == _user.value.displayNickname) {
            _updateNicknameState.value = UpdateNicknameState.Error("현재 닉네임과 동일합니다.")
            resetUpdateNicknameState()
            return
        }

        _updateNicknameState.value = UpdateNicknameState.Loading

        val userDocRef =
            FirebaseFirestore.getInstance().collection("users").document(firebaseUser.uid)

        userDocRef.update("nickname", newNickname)
            .addOnSuccessListener {
                _user.value = _user.value.copy(displayNickname = newNickname)
                _updateNicknameState.value = UpdateNicknameState.Success
                resetUpdateNicknameState()
            }
            .addOnFailureListener { exception ->
                _updateNicknameState.value = UpdateNicknameState.Error(
                    exception.message ?: "닉네임 업데이트에 실패했습니다."
                )
                resetUpdateNicknameState()
            }
    }

    private fun resetUpdateNicknameState() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(3000)
            _updateNicknameState.value = UpdateNicknameState.Idle
        }
    }

    sealed class UpdateNicknameState {
        object Idle : UpdateNicknameState()
        object Loading : UpdateNicknameState()
        object Success : UpdateNicknameState()
        data class Error(val message: String) : UpdateNicknameState()
    }

}