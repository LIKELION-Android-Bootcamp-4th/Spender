package com.e1i3.spender.feature.expense.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.e1i3.spender.feature.expense.domain.model.Emotion
import com.e1i3.spender.feature.mypage.domain.model.Category
import com.e1i3.spender.ui.theme.BlackColor
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import coil.compose.AsyncImage
import com.e1i3.spender.ui.theme.WhiteColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseContent(
    uiState: RegistrationUiState,
    viewModel: RegistrationViewModel,
    onManageCategoriesClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }
    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val datePickerState = rememberDatePickerState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onImageSelected(it)
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
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
                )
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

        HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)

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
                    text = uiState.category,
                    modifier = Modifier.padding(start = 20.dp),
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)

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

        HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)

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

        HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)

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
                )
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 34.dp, vertical = 24.dp)
        ) {
            Text(
                "사진",
                style = Typography.titleSmall
            )
            Spacer(Modifier.height(16.dp))

            if (uiState.selectedImageUri != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    AsyncImage(
                        model = uiState.selectedImageUri,
                        contentDescription = "첨부된 사진",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { galleryLauncher.launch("image/*") },
                    )
                    IconButton(
                        onClick = { viewModel.onImageSelectionCancelled() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp)
                            .background(BlackColor.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "사진 선택 취소",
                            tint = WhiteColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "사진 추가",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBottomSheetItem(
    category: Category,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        category.color?.let {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(it)
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(text = category.name, style = Typography.titleMedium)
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EmotionTagGroup(
    emotions: List<Emotion>,
    selectedEmotion: Emotion?,
    onEmotionSelected: (Emotion) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier.padding(start = 20.dp)
    ) {
        emotions.forEach { emotion ->
            val isSelected = emotion.id == selectedEmotion?.id
            FilterChip(
                selected = isSelected,
                onClick = { onEmotionSelected(emotion) },
                label = {
                    Text(
                        text = emotion.name,
                        color = if (isSelected) Color.White else BlackColor
                    )
                },
                shape = RoundedCornerShape(30),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    labelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedContainerColor = PointColor,
                    selectedLabelColor = Color.White
                ),
                border = if (isSelected) {
                    null
                } else {
                    BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            )
        }
    }
}

// 금액에 천 단위 쉼표
class NumberCommaTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val numberText = originalText.filter { it.isDigit() }
        if (numberText.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val number =
            numberText.toLongOrNull() ?: return TransformedText(text, OffsetMapping.Identity)
        val formattedText = DecimalFormat("#,###").format(number)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val originalSub = originalText.take(offset)
                val commasBefore = originalSub.count { !it.isDigit() }
                val digitsBefore = originalSub.count { it.isDigit() }

                var transformedOffset = 0
                var digitCount = 0
                for (char in formattedText) {
                    if (digitCount == digitsBefore) break
                    if (char.isDigit()) {
                        digitCount++
                    }
                    transformedOffset++
                }
                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                return formattedText.take(offset).count { it.isDigit() }
            }
        }
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}
