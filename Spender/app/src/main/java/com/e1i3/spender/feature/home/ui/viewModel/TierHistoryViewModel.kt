package com.e1i3.spender.feature.home.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.feature.home.domain.model.TierHistory
import com.e1i3.spender.feature.home.domain.repository.TierHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import java.util.Calendar

@HiltViewModel
class TierHistoryViewModel @Inject constructor(
    private val repository: TierHistoryRepository
) : ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _currentYear = mutableStateOf(Calendar.getInstance().get(Calendar.YEAR))
    val currentYear: State<Int> = _currentYear

    private val _tierList = mutableStateOf<List<TierHistory>>(emptyList())
    val tierList: State<List<TierHistory>> = _tierList

    fun loadTiers(year: Int){
        viewModelScope.launch {
            _isLoading.value = true
            _currentYear.value = year

            repository.getTierHistoryList(year)
                .onSuccess {  dtos ->
                    _tierList.value = dtos.map { dto ->
                        TierHistory(
                            month = dto.month,
                            level = dto.level
                        )
                    }
                }
        }
        _isLoading.value = false
    }

    fun goToPreviousYear() {
        loadTiers(_currentYear.value - 1)
    }

    fun goToNextYear() {
        loadTiers(_currentYear.value + 1)
    }

    fun setYear(year: Int){
        loadTiers(year)
    }
}