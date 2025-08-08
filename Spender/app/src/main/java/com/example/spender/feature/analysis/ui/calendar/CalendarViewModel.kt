package com.example.spender.feature.analysis.ui.calendar

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.example.spender.feature.analysis.domain.model.CalendarItemData
import com.example.spender.feature.analysis.domain.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository
): ViewModel(

) {
    val _calendarItem = MutableStateFlow<List<CalendarItemData>>(emptyList())
    val calendarItem: StateFlow<List<CalendarItemData>> = _calendarItem.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1500),
        initialValue = emptyList()
    )

    val now = Calendar.getInstance()
    var year: MutableState<Int> = mutableIntStateOf(now.get(Calendar.YEAR))
    var month: MutableState<Int> = mutableIntStateOf(now.get(Calendar.MONTH))
    val nowYear = now.get(Calendar.YEAR) //현재 날짜 set
    val nowMonth = now.get(Calendar.MONTH)
    val nowDay = now.get(Calendar.DATE)

    private val _selectionState = MutableStateFlow(listOf(0, 0, 0))
    val selectionState: StateFlow<List<Int>> = _selectionState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1500),
        initialValue = listOf(0, 0, 0)
    )

    val showDatePicker: MutableState<Boolean?> = mutableStateOf(null)

    private val _dailyList = MutableStateFlow<List<ExpenseDto>>(emptyList())
    val dailyList: StateFlow<List<ExpenseDto>> = _dailyList.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1500),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            setCalendar()
            setDialog()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            if (selectionState.value != listOf(0, 0, 0)) {
                setCalendar(year.value, month.value)
                updateSelectionByDay(_selectionState.value[2], _selectionState.value[0], _selectionState.value[1])
            }
        }
    }

    fun setCalendar(setYear: Int? = nowYear, setMonth: Int? = nowMonth) {
        viewModelScope.launch {
            now.set(Calendar.YEAR, setYear ?: year.value)
            now.set(Calendar.MONTH, setMonth ?: month.value)
            now.set(Calendar.DATE, 1)

            if (setYear != null) year.value = setYear
            if (setMonth != null) month.value = setMonth

            val calendarData = mutableListOf<CalendarItemData>()
            val startDay = when (now.get(Calendar.DAY_OF_WEEK)) {
                1 -> 6
                2 -> 0
                3 -> 1
                4 -> 2
                5 -> 3
                6 -> 4
                7 -> 5
                else -> -1
            }

            calendarData.clear()

            for (i in 0 until startDay) {
                calendarData.add(CalendarItemData(0, 0, false, false))
            }

            val expenseList = repository.getExpenseList(year.value, month.value+1)
            val incomeList = repository.getIncomeList(year.value, month.value+1)

            for (i in 1 .. now.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                var data = CalendarItemData(i, 0, false, false)
                val expense = expenseList.filter { it.date.toDate().date == i }
                val income = incomeList.filter { it.date.toDate().date == i }
                expense.forEach {
                    data = data.copy(expense = data.expense - it.amount)
                }
                income.forEach {
                    data = data.copy(expense = data.expense + it.amount)
                }
                if (expense.isNotEmpty() && income.isNotEmpty() && data.expense == 0) {
                    data = data.copy(expense = Int.MAX_VALUE)
                }
                if (i == nowDay && year.value == nowYear && month.value == nowMonth) {
                    calendarData.add(data.copy(background = false, today = true))
                    continue
                }
                calendarData.add(data)
            }

            _calendarItem.value = calendarData
            if (_selectionState.value == listOf(0, 0, 0)) {
                updateSelectionByDay(nowDay, year.value, month.value)
            }
        }
    }

    fun updateSelection(index: Int, year: Int, month: Int) {
        val calendarData = _calendarItem.value.toMutableList()
        if (calendarData[index].day != 0) {
            calendarData[index] = calendarData[index].copy(background = true)
            _calendarItem.value = calendarData
            _selectionState.value = listOf(year, month, calendarData[index].day)
        }
        getExpenseListByDate()
    }

    fun updateSelectionByDay(day: Int, year: Int, month: Int) {
        val calendarData = _calendarItem.value.toMutableList()
        val index = calendarData.indexOfFirst { it.day == day }
        if (calendarData[index].day != 0) {
            calendarData[index] = calendarData[index].copy(background = true)
            _calendarItem.value = calendarData
            _selectionState.value = listOf(year, month, calendarData[index].day)
        }
        getExpenseListByDate()
    }

    fun previousMonth() {
        viewModelScope.launch {
            month.value -= 1
            if (month.value < 0) {
                month.value = 11
                year.value -= 1
            }
            setCalendar(year.value, month.value)
        }
    }

    fun nextMonth() {
        viewModelScope.launch {
            month.value += 1
            if (month.value >= 12) {
                month.value = 0
                year.value += 1
            }
            setCalendar(year.value, month.value)
        }
    }

    private fun setDialog() {
        showDatePicker.value = false
    }

    fun showDialog() {
        showDatePicker.value = true
    }

    fun closeDialog() {
        showDatePicker.value = false
    }

    private fun getExpenseListByDate() {
        viewModelScope.launch {
            if (selectionState.value[2] == 0) {
                _dailyList.value = repository.getDailyList(year.value, month.value+1, nowDay)
            } else _dailyList.value = repository.getDailyList(year.value, month.value+1, selectionState.value[2])
        }
    }
}