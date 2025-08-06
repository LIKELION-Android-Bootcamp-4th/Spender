package com.example.spender.feature.report.ui.list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spender.feature.report.mapper.ReportMapper
import com.example.spender.feature.report.domain.model.Report
import com.example.spender.feature.report.domain.repository.ReportRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.util.Calendar

@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    // 현재 연도 상태
    private val _currentYear = mutableStateOf(Calendar.getInstance().get(Calendar.YEAR))
    val currentYear: State<Int> = _currentYear

    // 로딩 상태
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // 리포트 리스트 상태
    private val _reportList = mutableStateOf<List<Report>>(emptyList())
    val reportList: State<List<Report>> = _reportList

    // 에러 메시지
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadReports(year: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Log.w("ReportVM", "loadReports: UID is null")
            return
        }

        _isLoading.value = true
        _error.value = null

        repository.getReportList(
            year = year,
            onSuccess = { dtos ->
                _reportList.value = dtos.mapIndexed { index, dto ->
                    ReportMapper.fromListDto(dto, index)
                }
                _isLoading.value = false
                _currentYear.value = year
            },
            onError = { e ->
                _error.value = e.message
                _isLoading.value = false
            }
        )
    }

    fun goToPreviousYear() {
        loadReports(_currentYear.value - 1)
    }

    fun goToNextYear() {
        loadReports(_currentYear.value + 1)
    }
}