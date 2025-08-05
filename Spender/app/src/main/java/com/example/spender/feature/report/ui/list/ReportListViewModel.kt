package com.example.spender.feature.report.ui.list

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.spender.feature.report.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

}
