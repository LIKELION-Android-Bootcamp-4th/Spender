package com.example.spender.feature.expense.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.spender.ui.theme.NotoSansFamily
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseContent(
    uiState: RegistrationUiState,
    viewModel: RegistrationViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 금액 입력
        TextField(
            value = uiState.amount,
            onValueChange = { viewModel.onAmountChange(it) },
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

        Spacer(Modifier.height(12.dp))

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
                    .clickable { /* 카테고리 선택 로직 */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "취미/여가",
                    modifier = Modifier.padding(start = 20.dp),
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
            }
        }

        HorizontalDivider(color = Color(0xFFF0F2F5))

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
                    .clickable { /* 날짜 선택 로직 */ }
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "2025.07.28",
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
                    .height(120.dp),
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun EmotionTagGroup(
    emotions: List<String>,
    selectedEmotion: String,
    onEmotionSelected: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier.padding(start = 20.dp)
    ) {
        emotions.forEach { emotion ->
            val isSelected = emotion == selectedEmotion
            FilterChip(
                selected = isSelected,
                onClick = { onEmotionSelected(emotion) },
                label = {
                    Text(
                        text = emotion,
                        color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.8f)
                    )
                },
                shape = RoundedCornerShape(30),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    labelColor = Color.Black.copy(alpha = 0.8f),
                    selectedContainerColor = PointColor,
                    selectedLabelColor = Color.White
                ),
                border = if (isSelected) {
                    null
                } else {
                    BorderStroke(
                        width = 1.dp,
                        color = Color.LightGray
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

        val formattedText = try {
            val number = originalText.toLong()
            DecimalFormat("#,###").format(number)
        } catch (e: NumberFormatException) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val commas = formattedText.count { it == ',' }
                return offset + commas
            }

            override fun transformedToOriginal(offset: Int): Int {
                val commas = formattedText.substring(0, offset).count { it == ',' }
                return offset - commas
            }
        }
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ExpenseRegistrationScreenPreview() {
//    ExpenseRegistrationScreen()
//}