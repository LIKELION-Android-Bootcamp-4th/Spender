package com.example.spender.feature.expense.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.spender.ui.theme.NotoSansFamily
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringExpenseRegistrationScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(2) }
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var recurrence by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }


    val tabs = listOf("영수증", "지출", "정기지출")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("지출 등록", style = Typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { /* 뒤로가기 */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Button(
                //
                onClick = { /* 정기지출 등록 로직 */ },
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
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
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
            ){
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
                            fontFamily = NotoSansFamily,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                // 금액 입력
                TextField(
                    value = amount,
                    onValueChange = {
                        if (it.length <= 10) amount = it.filter { char -> char.isDigit() }
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
                    value = title,
                    onValueChange = { title = it },
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

            // 카테고리
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
                        .clickable { /* 반복기간 선택 로직 */ },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "매월 28일",
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
                    value = memo,
                    onValueChange = { memo = it },
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
}

@Preview(showBackground = true)
@Composable
fun RecurringExpenseRegistrationScreenPreview() {
    RecurringExpenseRegistrationScreen()
}