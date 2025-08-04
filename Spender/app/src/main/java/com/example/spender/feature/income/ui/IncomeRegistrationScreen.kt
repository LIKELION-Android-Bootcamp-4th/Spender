package com.example.spender.feature.income.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spender.feature.expense.ui.NumberCommaTransformation
import com.example.spender.ui.theme.NotoSansFamily
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.SpenderTheme
import com.example.spender.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeRegistrationScreen(){
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("수입 등록", style = Typography.titleMedium) },
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
                onClick = { /* 수입 등록 로직 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PointColor,
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    "수입 등록",
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
        ){
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                // 금액 입력
                TextField(
                    value = amount,
                    onValueChange = {
                        amount = it.filter { c -> c.isDigit() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 16.dp),
                    placeholder = { Text("수입을 입력하세요", fontSize = 14.sp, color = Color.Gray) },
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
                    onValueChange = {title = it},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("수입 내용을 입력하세요", fontSize = 16.sp, color = Color.Gray) },
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

            //날짜
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

            // 메모
            Column(modifier = Modifier.padding(horizontal = 34.dp, vertical = 24.dp)) {
                Text(
                    "메모",
                    style = Typography.titleSmall,
                )
                Spacer(Modifier.height(30.dp))
                OutlinedTextField(
                    value = memo,
                    onValueChange = {memo = it},
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
fun IncomeRegistrationScreenPreview() {
    SpenderTheme {
        IncomeRegistrationScreen()
    }
}