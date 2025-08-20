package com.e1i3.spender.feature.home.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.feature.home.domain.repository.FriendDetailRepository
import com.e1i3.spender.feature.home.mapper.toUiModel
import com.e1i3.spender.feature.home.ui.model.FriendDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FriendDetailViewModel @Inject constructor(
    private val repository: FriendDetailRepository
):ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _friendDetail = mutableStateOf<FriendDetailUiModel?>(null)
    val friendDetail: State<FriendDetailUiModel?> = _friendDetail

    fun loadFriendDetail(friendId: String){
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getFriendDetail(friendId)
                .onSuccess { dto ->
                    _friendDetail.value = dto.toUiModel()
                }
                .onFailure { error ->
                    _error.value = error.message
                }
            _isLoading.value = false
        }
    }
}