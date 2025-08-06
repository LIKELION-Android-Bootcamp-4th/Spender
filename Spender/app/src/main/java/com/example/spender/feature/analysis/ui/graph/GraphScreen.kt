package com.example.spender.feature.analysis.ui.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.feature.analysis.ui.SpendListItemComponent
import com.example.spender.ui.theme.NotoSansFamily
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val viewModel: GraphViewModel = hiltViewModel()

    val graphData by viewModel.graphData.collectAsState()
    val dateData by viewModel.dateData.collectAsState()
    val showDatePicker = viewModel.showDatePicker.value

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {
        Column (modifier = Modifier.padding(horizontal = 24.dp)) {
            //header
            if (graphData.isEmpty()) {
                Text("이번 달에는 지출이 없어요", style = Typography.titleMedium)
            } else {
                Text("이번 달 중 가장 지출이 많았던 날은", style = Typography.titleMedium)
                Row {
                    Text("${dateData[1]}월 ${graphData.maxByOrNull { it.expense }?.day ?: 0}일 ",
                        style = TextStyle(fontFamily = NotoSansFamily, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PointColor))
                    Text("이에요", style = Typography.titleMedium)
                }
                Spacer(Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("${dateData[1]}월", style = Typography.titleMedium)
                    Spacer(Modifier.width(10.dp))
                    Text("${dateData[0]}", style = Typography.bodyMedium)
                    Spacer(Modifier.width(10.dp))
                    IconButton(
                        onClick = { viewModel.showDialog() },
                        modifier = Modifier.size(20.dp).align(Alignment.Bottom)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "datePicker",
                            tint = Color.DarkGray
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = { viewModel.previousMonth() },
                        modifier = Modifier.size(20.dp).align(Alignment.Bottom)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "previous month",
                            tint = Color.DarkGray
                        )
                    }
                    IconButton(
                        onClick = { viewModel.nextMonth() },
                        modifier = Modifier.size(20.dp).align(Alignment.Bottom)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "next month",
                            tint = Color.DarkGray
                        )
                    }
                }
                //body
                LineChart(graphData)
                Spacer(Modifier.height(20.dp))
                Text("이번 달 가장 컸던 지출", style = Typography.titleMedium)
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                SpendListItemComponent(viewModel.getMaxExpense())
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
                                viewModel.setMonth(
                                    yyyyMMdd.substring(0, 4).toInt(),
                                    yyyyMMdd.substring(4, 6).toInt()
                                )
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
}