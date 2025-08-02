package com.example.spender.feature.analysis.ui.calendar

import android.app.Application
import android.icu.util.Calendar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.feature.analysis.domain.model.CalendarItemData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CalendarViewModel(application: Application): AndroidViewModel(application) {
    val _calendarItem = MutableStateFlow<List<CalendarItemData>>(emptyList())
    val calendarItem: StateFlow<List<CalendarItemData>> = _calendarItem.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val now = Calendar.getInstance()
    var year: MutableState<Int> = mutableIntStateOf(now.get(Calendar.YEAR))
    var month: MutableState<Int> = mutableIntStateOf(now.get(Calendar.MONTH))
    val nowYear = now.get(Calendar.YEAR) //현재 날짜 set
    val nowMonth = now.get(Calendar.MONTH)
    val nowDay = now.get(Calendar.DATE)

    val _selectionState = MutableStateFlow<List<Int>>(listOf(0, 0, 0))
    val selectionState: StateFlow<List<Int>> = _selectionState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf(0, 0, 0)
    )

    val showDatePicker: MutableState<Boolean?> = mutableStateOf(null)

    init {
        setCalendar()
        setDialog()
    }

    fun setCalendar(setYear: Int? = nowYear, setMonth: Int? = nowMonth) {
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

        for (i in 1 .. now.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            if (year.value == nowYear && month.value == nowMonth && i == nowDay) {
                calendarData.add(CalendarItemData(i, 0, false, true))
                continue
            }
            calendarData.add(CalendarItemData(i, 0, false, false))
            //TODO: 통신 연결 시 이 부분에서 기록을 날짜와 매칭할 필요 있음
        }

        _calendarItem.value = calendarData
    }

    fun updateSelection(index: Int, year: Int, month: Int) {
        val calendarData = _calendarItem.value.toMutableList()
        if (calendarData[index].day != 0) {
            calendarData[index] = calendarData[index].copy(background = true)
            _calendarItem.value = calendarData
            _selectionState.value = listOf(year, month, calendarData[index].day)
        }
    }

    fun updateSelectionByDay(day: Int, year: Int, month: Int) {
        val calendarData = _calendarItem.value.toMutableList()
        val index = calendarData.indexOfFirst { it.day == day }
        if (calendarData[index].day != 0) {
            calendarData[index] = calendarData[index].copy(background = true)
            _calendarItem.value = calendarData
            _selectionState.value = listOf(year, month, calendarData[index].day)
        }
    }

    fun previousMonth() {
        month.value -= 1
        if (month.value < 0) {
            month.value = 11
            year.value -= 1
        }
        setCalendar(month.value, year.value)
    }

    fun nextMonth() {
        month.value += 1
        if (month.value >= 12) {
            month.value = 0
            year.value += 1
        }
        setCalendar(year.value, month.value)
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
}