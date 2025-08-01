package com.example.spender.feature.analysis.ui.calendar

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.util.Calendar

@Composable
fun CalendarScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val viewModel: CalendarViewModel = viewModel(
        factory = CalendarViewModelFactory(context.applicationContext as Application)
    )

    val calendarData by viewModel.calendarItem.collectAsState()
    val selectionState by viewModel.selectionState.collectAsState()
    val calendar = Calendar.getInstance()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(horizontal = 24.dp)
    ) {
        Column {
            CalendarHeader(viewModel.year, viewModel.month,
                { /* TODO show datepicker */ },
                { viewModel.previousMonth() },
                { viewModel.nextMonth() }
            )
            Spacer(Modifier.height(10.dp))
            Calendar(calendarData,  {
                viewModel.setCalendar(viewModel.year, viewModel.month)
                viewModel.updateSelection(it, viewModel.year, viewModel.month)
            }, selectionState, viewModel.year, viewModel.month)
            Spacer(Modifier.height(10.dp))
            if (selectionState == listOf(0, 0, 0)) {
                SpendListByDate(viewModel.nowMonth, viewModel.nowDay, viewModel.now.get(Calendar.DAY_OF_WEEK) - 1)
            } else {
                calendar.set(Calendar.YEAR, selectionState[0])
                calendar.set(Calendar.MONTH, selectionState[1])
                SpendListByDate(selectionState[1], selectionState[2], (calendar.get(Calendar.DAY_OF_WEEK) + selectionState[2] - 2) % 7)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
}