package com.e1i3.spender.feature.analysis.ui.graph

import android.icu.util.Calendar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.core.data.remote.expense.ExpenseDto
import com.e1i3.spender.feature.analysis.domain.model.CalendarItemData
import com.e1i3.spender.feature.analysis.domain.repository.GraphRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GraphViewModel @Inject constructor(
    private val repository: GraphRepository
): ViewModel() {
    private val _graphData = MutableStateFlow<List<CalendarItemData>>(emptyList())
    val graphData: StateFlow<List<CalendarItemData>> = _graphData.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1500),
        initialValue = emptyList()
    )

    private val _maxExpense = MutableStateFlow<ExpenseDto?>(null)
    val maxExpense: StateFlow<ExpenseDto?> = _maxExpense.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1500),
        initialValue = null
    )

    var sampleData = listOf( //테스트용
        CalendarItemData(5, 10000),
        CalendarItemData(10, 20000),
        CalendarItemData(20, 35000),
        CalendarItemData(31, 15000)
    )

    val now = Calendar.getInstance()
    var year = now.get(Calendar.YEAR)
    var month = now.get(Calendar.MONTH)

    private val _dateData = MutableStateFlow(listOf(year, month+1))
    val dateData: StateFlow<List<Int>> = _dateData.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1500),
        initialValue = listOf(year, month+1)
    )

    val showDatePicker: MutableState<Boolean?> = mutableStateOf(null)

    init {
        getDailyExpense()
        getMaxExpense()
    }

    fun refresh() {
        getDailyExpense()
        getMaxExpense()
    }

    fun previousMonth() {
        month -= 1
        if (month < 0) {
            month = 11
            year -= 1
        }
        _dateData.value = listOf(year, month+1)
        getDailyExpense()
        getMaxExpense()
    }

    fun nextMonth() {
        month += 1
        if (month >= 12) {
            month = 0
            year += 1
        }
        _dateData.value = listOf(year, month+1)
        getDailyExpense()
        getMaxExpense()
    }

    fun setMonth(year: Int, month: Int) {
        this.year = year
        this.month = month-1
        _dateData.value = listOf(year, month)
    }

    fun showDialog() {
        showDatePicker.value = true
    }

    fun closeDialog() {
        showDatePicker.value = false
    }

    private fun getMaxExpense() {
        viewModelScope.launch {
            _maxExpense.value = repository.getMaxExpense(year, month)
        }
    }

    private fun getDailyExpense() {
        viewModelScope.launch {
            val rawData = repository.getDailyTotalList(year, month)
            val dataList = mutableListOf<CalendarItemData>()
            for (doc in rawData) {
                dataList.add(CalendarItemData(day = doc.key.toInt(), expense = doc.value))
            }

            _graphData.value = dataList
        }
    }
}