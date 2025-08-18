package com.example.spender.feature.analysis.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.example.spender.feature.analysis.domain.model.SpendListItemData
import com.example.spender.feature.analysis.ui.SpendListItemComponent
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.navigation.Screen

@Composable
fun SpendListByDate(month: Int, day: Int, dayOfWeek: Int, list: List<ExpenseDto>, navHostController: NavHostController) { //캘린더 하단 지출 리스트
    val itemData = mutableListOf<SpendListItemData>()
    for (data in list) {
        itemData.add(SpendListItemData(
            data.title,
            data.amount,
            data.amount > 0
        ))
    }
    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = when(dayOfWeek) {
                0 -> "토요일"
                1 -> "일요일"
                2 -> "월요일"
                3 -> "화요일"
                4 -> "수요일"
                5 -> "목요일"
                6 -> "금요일"
                else -> ""
            }, style = Typography.titleMedium)
            Spacer(Modifier.width(10.dp))
            Text(text = "${month+1}월 ${day}일", style = Typography.bodyMedium)
        }
        Spacer(Modifier.height(10.dp))
        LazyColumn {
            itemsIndexed(list) { _, item ->
                SpendListItemComponent(item, {
                    if (item.amount > 0) {
                        navHostController.navigate(
                            Screen.IncomeDetailScreen.createRoute(item.id)
                        )
                    } else {
                        navHostController.navigate(
                            Screen.ExpenseDetailScreen.createRoute(item.id)
                        )
                    }
                })
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}