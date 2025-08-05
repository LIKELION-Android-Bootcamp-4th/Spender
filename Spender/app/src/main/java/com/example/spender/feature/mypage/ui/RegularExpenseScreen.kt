package com.example.spender.feature.mypage.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.feature.mypage.domain.model.RegularExpense
import com.example.spender.feature.mypage.ui.component.RegularExpenseListItem
import com.example.spender.ui.theme.Typography

@Composable
fun RegularExpenseScreen(navHostController: NavHostController) {

    // 데이터 샘플!!!!!
    val sampleExpenses = listOf(
        RegularExpense(1, "넷플릭스 정기 결제", "매월 28일"),
        RegularExpense(2, "정기결제1", "매년 12월 28일"),
        RegularExpense(3, "정기 지출2", "매주 토요일"),
        RegularExpense(4, "정기 지출3", "매일"),
        RegularExpense(5, "스포티파이 구독", "매월 15일")
    )

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "정기지출 모아보기",
                navHostController,
                showBackButton = true,
                actions = {
                    IconButton(
                        onClick = {
                            // TODO: 정기지출 추가 화면으로 연결
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "추가하기"
                        )
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                if (sampleExpenses.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "등록된 정기 지출이 없습니다",
                                style = Typography.bodyMedium
                            )
                        }
                    }
                } else {
                    itemsIndexed(sampleExpenses) { index, expense ->
                        RegularExpenseListItem(
                            title = expense.title,
                            date = expense.date
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RegularExpenseScreenPreview() {
    RegularExpenseScreen(navHostController = rememberNavController())
}
