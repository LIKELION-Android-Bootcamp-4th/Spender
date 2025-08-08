package com.example.spender.feature.expense.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spender.ui.theme.Typography
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringExpenseContent(
    uiState: RegistrationUiState,
    viewModel: RegistrationViewModel,
    onManageCategoriesClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }
    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val datePickerState = rememberDatePickerState()

    //반복 날짜 바텀시트
    if (uiState.isRepeatSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onRepeatSheetVisibilityChange(false) },
            sheetState = sheetState
        ) {
            RepeatDaySelectionSheet(
                onDaySelected = { day ->
                    viewModel.onRepeatDaySelected(day)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            viewModel.onRepeatSheetVisibilityChange(false)
                        }
                    }
                }
            )
        }
    }
    // 카테고리 바텀시트
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("카테고리 선택", style = Typography.labelMedium)
                    TextButton(
                        onClick = {
                            onManageCategoriesClick()
                        }
                    ) {
                        Text("관리", style = Typography.labelMedium)
                    }
                }
                Spacer(Modifier.height(16.dp))
                LazyColumn {
                    items(expenseCategories) { category ->
                        CategoryBottomSheetItem(
                            category = category,
                            onClick = {
                                viewModel.onCategorySelected(category)
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        isSheetOpen = false
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    //날짜 선택 다이얼로그
    if (uiState.isDatePickerDialogVisible) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onDateDialogVisibilityChange(false) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onDateSelected(datePickerState.selectedDateMillis)
                        viewModel.onDateDialogVisibilityChange(false)
                    }
                ) { Text("확인") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDateDialogVisibilityChange(false) }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            // 금액 입력
            TextField(
                value = uiState.amount,
                onValueChange = {
                    viewModel.onAmountChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 16.dp),
                placeholder = { Text("지출을 입력하세요", fontSize = 14.sp, color = Color.Gray) },
                trailingIcon = { Text("원", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = NumberCommaTransformation(),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary
                )
            )
            // 내용 입력
            TextField(
                value = uiState.title,
                onValueChange = {viewModel.onTitleChange(it)},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("정기지출 내용을 입력하세요", fontSize = 16.sp, color = Color.Gray) },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )
        }

        HorizontalDivider(color = Color(0xFFF0F2F5))

        // 카테고리
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 34.dp, vertical = 24.dp)
        ) {
            Text(
                text = "카테고리",
                style = Typography.titleSmall,
            )
            Spacer(Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {isSheetOpen = true},
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.category,
                    modifier = Modifier.padding(start = 20.dp),
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
            }
        }

        HorizontalDivider(color = Color(0xFFF0F2F5))

        //시작 일
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 34.dp, vertical = 24.dp)
        ) {
            Text(
                text = "시작 일",
                style = Typography.titleSmall,
            )
            Spacer(Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* 날짜 선택 로직 */ }
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                Text(
                    text = dateFormat.format(uiState.date),
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "날짜 선택",
                    tint = Color.Black
                )
            }
        }

        HorizontalDivider(color = Color(0xFFF0F2F5))

        // 반복 날짜
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 34.dp, vertical = 24.dp)
        ) {
            Text(
                text = "반복",
                style = Typography.titleSmall,
            )
            Spacer(Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onRepeatSheetVisibilityChange(true) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "매월 ${uiState.dayOfMonth}일",
                    modifier = Modifier.padding(start = 20.dp),
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
            }
        }

        HorizontalDivider(color = Color(0xFFF0F2F5))


        // 메모
        Column(modifier = Modifier.padding(horizontal = 34.dp, vertical = 24.dp)) {
            Text(
                "메모",
                style = Typography.titleSmall,
            )
            Spacer(Modifier.height(30.dp))
            OutlinedTextField(
                value = uiState.memo,
                onValueChange = {viewModel.onMemoChange(it)},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                placeholder = {
                    Text(
                        "메모",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
        }
    }
}

@Composable
private fun RepeatDaySelectionSheet(onDaySelected: (Int) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("반복 날짜 선택", style = Typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.navigationBarsPadding()) {
            items(31) { day ->
                Text(
                    text = "${day + 1}일",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDaySelected(day + 1) }
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RecurringExpenseRegistrationScreenPreview() {
//    RecurringExpenseRegistrationScreen()
//}