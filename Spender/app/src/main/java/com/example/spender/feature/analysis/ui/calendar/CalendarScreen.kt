package com.example.spender.feature.analysis.ui.calendar

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.spender.ui.theme.PointColor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navHostController: NavHostController) {
    val viewModel: CalendarViewModel = hiltViewModel()

    val calendarData by viewModel.calendarItem.collectAsState()
    val selectionState by viewModel.selectionState.collectAsState()
    val dailyList by viewModel.dailyList.collectAsState()
    val calendar = Calendar.getInstance()
    val showDatePicker = viewModel.showDatePicker.value
    val year = viewModel.year.value
    val month = viewModel.month.value

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(horizontal = 24.dp)
    ) {
        Column {
            CalendarHeader(year, month,
                { viewModel.showDialog() },
                { viewModel.previousMonth() },
                { viewModel.nextMonth() }
            )
            Spacer(Modifier.height(10.dp))
            Calendar(calendarData,  {
                viewModel.setCalendar(year, month)
                viewModel.updateSelection(it, year, month)
            }, selectionState, year, month)
            Spacer(Modifier.height(10.dp))
            if (selectionState == listOf(0, 0, 0)) {
                SpendListByDate(
                    viewModel.nowMonth,
                    viewModel.nowDay,
                    (viewModel.now.get(Calendar.DAY_OF_WEEK) + viewModel.now.get(Calendar.DATE) + 2) % 7,
                    dailyList
                )
            } else {
                calendar.set(Calendar.YEAR, selectionState[0])
                calendar.set(Calendar.MONTH, selectionState[1])
                SpendListByDate(
                    selectionState[1],
                    selectionState[2],
                    (calendar.get(Calendar.DAY_OF_WEEK) + selectionState[2] + 1) % 7,
                    dailyList
                )
            }
        }

        val dialogState = rememberDatePickerState()
        dialogState.selectedDateMillis = System.currentTimeMillis()
        if (showDatePicker == true) {
            DatePickerDialog(
                onDismissRequest = { viewModel.closeDialog() },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val yyyyMMdd = SimpleDateFormat(
                                "yyyyMMdd",
                                Locale.getDefault()
                            ).format(
                                Date(
                                    dialogState.selectedDateMillis ?: System.currentTimeMillis()
                                )
                            )
                            viewModel.setCalendar(
                                yyyyMMdd.substring(0, 4).toInt(),
                                yyyyMMdd.substring(4, 6).toInt()-1
                            )
                            viewModel.updateSelectionByDay(yyyyMMdd.substring(6, 8).toInt(),
                                yyyyMMdd.substring(0, 4).toInt(),
                                yyyyMMdd.substring(4, 6).toInt()-1)
                            viewModel.closeDialog()
                        },
                    ) {
                        Text("확인")
                    }
                },
                modifier = Modifier.alpha(1f),
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.closeDialog() }
                    ) {
                        Text("취소")
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White.copy(),
                    selectedDayContainerColor = PointColor,
                    todayDateBorderColor = PointColor,
                    selectedYearContainerColor = PointColor,
                    disabledSelectedDayContainerColor = PointColor,
                    disabledSelectedYearContainerColor = PointColor,
                    dayContentColor = Color.Black,
                ),
            ) {
                DatePicker(
                    state = dialogState,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.White.copy(),
                        selectedDayContainerColor = PointColor,
                        todayDateBorderColor = PointColor,
                        selectedYearContainerColor = PointColor,
                        disabledSelectedDayContainerColor = PointColor,
                        disabledSelectedYearContainerColor = PointColor,
                        dayContentColor = Color.Black,
                    ),
                )
            }
        }
    }
}