package com.e1i3.spender.feature.home.ui.viewModel

import androidx.lifecycle.ViewModel
import com.e1i3.spender.feature.home.domain.repository.FriendDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class FriendDetailViewModel @Inject constructor(
    private val repository: FriendDetailRepository
):ViewModel() {

}