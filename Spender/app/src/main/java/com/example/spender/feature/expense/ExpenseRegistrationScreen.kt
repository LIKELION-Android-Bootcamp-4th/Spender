package com.example.spender.feature.expense

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
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseRegistrationScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(1) }
    var amount by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }
    var selectedEmotion by remember { mutableStateOf("만족") }

    val tabs = listOf("영수증", "지출", "정기지출")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("지출 등록", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* 뒤로가기 */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Button( //
                onClick = { /* 지출 등록 로직 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("지출 등록", modifier = Modifier.padding(vertical = 6.dp), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 탭 메뉴
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(10.dp)),
                containerColor = Color(0xFFF0F2F5),
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .fillMaxSize()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .height(48.dp)
                            .zIndex(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { selectedTabIndex = index }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            color = if (isSelected) Color.Black else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            // 금액 입력
            TextField(
                value = amount,
                onValueChange = { if (it.length <= 10) amount = it.filter { char -> char.isDigit() } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 16.dp ),
                placeholder = { Text("지출을 입력하세요", fontSize = 14.sp, color = Color.Gray) },
                trailingIcon = { Text("원", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = NumberCommaTransformation(),
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Start),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(color = Color(0xFFF0F2F5))

            // 카테고리/날짜 선택
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "카테고리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* 카테고리 선택 로직 */ },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "취미/여가",
                        modifier = Modifier.padding(start = 20.dp),
                        style = MaterialTheme.typography.bodyLarge
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
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* 날짜 선택 로직 */ }
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "2025.07.28",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.width(12.dp))
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "날짜 선택", tint = Color.Gray)
                }
            }

            HorizontalDivider(color = Color(0xFFF0F2F5))

            // 감정 태그
            Column(modifier = Modifier.padding(horizontal = 34.dp, vertical = 24.dp)) {
                Text("감정 태그", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                EmotionTagGroup(
                    emotions = listOf("만족", "불만", "충동", "억울"),
                    selectedEmotion = selectedEmotion,
                    onEmotionSelected = { selectedEmotion = it }
                )
            }

            HorizontalDivider(color = Color(0xFFF0F2F5))

            // 메모
            Column(modifier = Modifier.padding(horizontal = 34.dp, vertical = 24.dp)) {
                Text("메모", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = memo,
                    onValueChange = { memo = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("메모", color = Color.Gray, fontSize = 14.sp) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )
            }
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        emotions.forEach { emotion ->
            val isSelected = emotion == selectedEmotion
            FilterChip(
                selected = isSelected,
                onClick = { onEmotionSelected(emotion) },
                label = { Text(emotion) },
                shape = RoundedCornerShape(50),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFFF0F2F5),
                    labelColor = Color.Gray,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White
                ),
                border = null
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

@Preview(showBackground = true)
@Composable
fun ExpenseRegistrationScreenPreview() {
    ExpenseRegistrationScreen()
}