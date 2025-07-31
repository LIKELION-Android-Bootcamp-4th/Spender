package com.example.spender.feature.analysis

import android.app.Application
import android.icu.util.Calendar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.feature.analysis.model.CalendarItemData
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
    val _selectionState = MutableStateFlow<List<Int>>(listOf(0, 0, 0))
    val selectionState: StateFlow<List<Int>> = _selectionState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf(0, 0, 0)
    )

    val now = Calendar.getInstance()
    var year = now.get(Calendar.YEAR)
    var month = now.get(Calendar.MONTH)
    private val nowYear = now.get(Calendar.YEAR)
    private val nowMonth = now.get(Calendar.MONTH)
    private val nowDay = now.get(Calendar.DATE)

    init {
        setCalendar()
    }

    fun setCalendar(setYear: Int? = null, setMonth: Int? = null) {
        year = setYear ?: nowYear
        month = setMonth ?: nowMonth

        now.set(Calendar.YEAR, year)
        now.set(Calendar.MONTH, month)
        now.set(Calendar.DATE, 1)

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
            if (year == nowYear
                && month == nowMonth
                && i == nowDay
            ) {
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
        }

        _calendarItem.value = calendarData
        _selectionState.value = listOf(year, month, calendarData[index].day)
    }
}