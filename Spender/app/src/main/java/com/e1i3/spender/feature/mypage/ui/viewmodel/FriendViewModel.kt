package com.e1i3.spender.feature.mypage.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.e1i3.spender.feature.mypage.data.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val repository: FriendRepository
): ViewModel() {
    private val _myCode = MutableStateFlow("")
    val myCode = _myCode.asStateFlow()

    private val _yourCode = MutableStateFlow("")
    val yourCode = _yourCode.asStateFlow()

    private val _expiredAt = MutableStateFlow("")
    val expiredAt = _expiredAt.asStateFlow()

    fun onInput(newCode: String) {
        _yourCode.update { newCode }
    }

    init {
        getCode()
    }

    private fun getCode() {
        viewModelScope.launch {
            val code = repository.getCode()
            val formatter = SimpleDateFormat("yyyy-MM-dd HH시 mm분", Locale.KOREA)
            _myCode.update { code.first }
            _expiredAt.update { formatter.format(code.second.toDate()) }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val code = repository.refresh()
            val formatter = SimpleDateFormat("yyyy-MM-dd HH시 mm분", Locale.KOREA)
            _myCode.update { code.first }
            _expiredAt.update { formatter.format(code.second.toDate()) }
        }
    }

    fun addFriend(showMessage: (msg: String) -> Unit) {
        viewModelScope.launch {
            if (yourCode.value.trim().length < 8 || Regex("[^A-Z0-9]").containsMatchIn(yourCode.value.trim())) {
                showMessage("올바르지 않은 코드입니다.")
                return@launch
            }
            showMessage(repository.addFriend(yourCode.value))
        }
    }
}