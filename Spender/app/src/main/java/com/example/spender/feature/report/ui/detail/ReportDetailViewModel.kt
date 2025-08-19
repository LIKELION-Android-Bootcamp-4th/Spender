package com.example.spender.feature.report.ui.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.feature.report.domain.model.ReportDetail
import com.example.spender.feature.report.domain.repository.ReportRepository
import com.example.spender.feature.report.mapper.ReportMapper.fromDetailDto
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ReportDetailViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel(){

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _reportDetail = mutableStateOf(
        ReportDetail(
            month = "",
            totalExpense = 0,
            totalBudget = 0,
            feedback = "",
            byCategory = emptyList(),
            byEmotion = emptyList()
        )
    )
    val reportDetail : State<ReportDetail> = _reportDetail

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadReportDetail(month: String){
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getReportDetail(month = month)
            result.fold(
                onSuccess = { report ->
                    _reportDetail.value = fromDetailDto(report)
                    _isLoading.value = false
                },
                onFailure = { e ->
                    _error.value = e.message
                    _isLoading.value = false
                }
            )
        }
    }
}