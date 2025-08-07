package com.example.spender.feature.report.ui.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spender.feature.report.domain.model.ReportDetail
import com.example.spender.feature.report.domain.repository.ReportRepository
import com.example.spender.feature.report.mapper.ReportMapper.fromDetailDto
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ReportDetailViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel(){

    // 로딩 상태
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // 리포트 상태
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

    // 에러 메시지
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadReportDetail(month: String){

        _isLoading.value = true
        _error.value = null
        
        repository.getReportDetail(
            month = month,
            onSuccess = { report ->
                _reportDetail.value = fromDetailDto(report)
                _isLoading.value = false
            },
            onError = { e ->
                _error.value = e.message
                _isLoading.value = false
            }
        )
    }
}