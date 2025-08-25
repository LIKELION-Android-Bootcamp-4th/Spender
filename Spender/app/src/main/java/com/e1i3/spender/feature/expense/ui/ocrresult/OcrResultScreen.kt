package com.e1i3.spender.feature.expense.ui.ocrresult

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.feature.expense.ui.CategoryBottomSheetItem
import com.e1i3.spender.feature.expense.ui.EmotionTagGroup
import com.e1i3.spender.feature.expense.ui.NumberCommaTransformation
import com.e1i3.spender.feature.expense.ui.RegistrationEvent
import com.e1i3.spender.ui.theme.NotoSansFamily
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography
import com.e1i3.spender.ui.theme.navigation.Screen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcrResultScreen(
    navHostController: NavHostController,
    title: String,
    amount: String,
    date: String,
    viewModel: OcrResultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var isSheetOpen by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is RegistrationEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is RegistrationEvent.NavigateBack -> {
                    navHostController.popBackStack()
                }

                else -> {}
            }
        }
    }
    //바텀시트
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
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
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    isSheetOpen = false
                                    navHostController.navigate(Screen.ExpenseCategoryScreen.route)
                                }
                            }
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

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "영수증 인식",
                navHostController,
                showBackButton = true,
            )
        },
        bottomBar = {
            Button(
                onClick = { viewModel.onRegisterClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PointColor,
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    "지출 등록",
                    modifier = Modifier.padding(vertical = 6.dp),
                    fontSize = 20.sp,
                    fontFamily = NotoSansFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            //상단 안내 문구
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("영수증 정보를 확인해주세요.", style = Typography.titleSmall)
                Spacer(Modifier.height(4.dp))
                Text(
                    "영수증 정보를 확인하고 직접 수정하거나 재촬영하세요",
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                //지출 내용
                TextField(
                    value = uiState.title,
                    onValueChange = { viewModel.onTitleChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 16.dp),
                    placeholder = {
                        Text(
                            "지출 제목을 입력하세요",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    },
                    singleLine = true,
                    textStyle = Typography.titleMedium,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // 금액 입력
                TextField(
                    value = uiState.amount,
                    onValueChange = { viewModel.onAmountChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 16.dp),
                    placeholder = {
                        Text(
                            "지출을 입력하세요",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    },
                    trailingIcon = { Text("원", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    visualTransformation = NumberCommaTransformation(),
                    singleLine = true,
                    textStyle = Typography.titleMedium,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

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
                        .clickable { isSheetOpen = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiState.categoryName,
                        modifier = Modifier.padding(start = 20.dp),
                        style = Typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "날짜",
                    style = Typography.titleSmall,
                )
                Spacer(Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onDateDialogVisibilityChange(true) }
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
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            // 감정 태그
            Column(modifier = Modifier.padding(horizontal = 34.dp, vertical = 24.dp)) {
                Text(
                    "감정 태그",
                    style = Typography.titleSmall,
                )
                Spacer(Modifier.height(30.dp))
                EmotionTagGroup(
                    emotions = uiState.emotions,
                    selectedEmotion = uiState.selectedEmotion,
                    onEmotionSelected = { viewModel.onEmotionSelected(it) }
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            // 메모
            Column(modifier = Modifier.padding(horizontal = 34.dp, vertical = 24.dp)) {
                Text(
                    "메모",
                    style = Typography.titleSmall,
                )
                Spacer(Modifier.height(30.dp))
                OutlinedTextField(
                    value = uiState.memo,
                    onValueChange = { viewModel.onMemoChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    placeholder = {
                        Text(
                            "메모",
                            style = Typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontSize = 14.sp
                        )
                    },
                    textStyle = Typography.bodyMedium,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.tertiary
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                )
            }
        }
    }
}