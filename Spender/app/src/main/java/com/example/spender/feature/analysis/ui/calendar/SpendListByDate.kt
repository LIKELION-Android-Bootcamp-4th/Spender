package com.example.spender.feature.analysis.ui.calendar

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.example.spender.feature.analysis.domain.model.CalendarItemData
import com.example.spender.feature.analysis.domain.model.SpendListItemData
import com.example.spender.feature.analysis.ui.SpendListItemComponent
import com.example.spender.ui.theme.NotoSansFamily
import com.example.spender.ui.theme.Typography

@Composable
fun SpendListByDate(month: Int, day: Int, dayOfWeek: Int, list: MutableList<ExpenseDto>) { //캘린더 하단 지출 리스트
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
                0 -> "일요일"
                1 -> "월요일"
                2 -> "화요일"
                3 -> "수요일"
                4 -> "목요일"
                5 -> "금요일"
                6 -> "토요일"
                else -> ""
            }, style = Typography.titleMedium)
            Spacer(Modifier.width(10.dp))
            Text(text = "${month+1}월 ${day}일", style = Typography.bodyMedium)
        }
        Spacer(Modifier.height(10.dp))
        LazyColumn {
            itemsIndexed(list) { index, item ->
                SpendListItemComponent(item)
                if (index != itemData.lastIndex) {
                    Spacer(Modifier.height(5.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    }
}