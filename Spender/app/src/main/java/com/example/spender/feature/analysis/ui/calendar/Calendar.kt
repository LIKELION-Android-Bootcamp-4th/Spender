package com.example.spender.feature.analysis.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spender.feature.analysis.domain.model.CalendarItemData
import com.example.spender.ui.theme.DefaultFontColor
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.TabColor
import com.example.spender.ui.theme.Typography

@Composable
fun CalendarHeader( //캘린더 상단 화살표 및 연월표시
    year: Int,
    month: Int,
    clickDatePicker: () -> Unit,
    clickPrevious: () -> Unit,
    clickNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${month+1}월",
            style = Typography.titleMedium,
            modifier = Modifier.alignByBaseline()
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "$year",
            style = Typography.bodyMedium,
            modifier = Modifier.alignByBaseline()
        )
        Spacer(Modifier.width(8.dp))
        IconButton(
            onClick = { clickDatePicker() },
            modifier = Modifier.size(20.dp).align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "datePicker",
                tint = Color.DarkGray
            )
        }
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = { clickPrevious() },
            modifier = Modifier.size(20.dp).align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "previous month",
                tint = Color.DarkGray
            )
        }
        IconButton(
            onClick = { clickNext() },
            modifier = Modifier.size(20.dp).align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "next month",
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
fun Calendar(data: List<CalendarItemData>, onClick: (Int) -> Unit, selection: List<Int>, year: Int, month: Int) { //캘린더
    val day = listOf("월", "화", "수", "목", "금", "토", "일")
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        items(day) { item ->
            Text(text = item,
                modifier = Modifier.aspectRatio(1f),
                textAlign = TextAlign.Center
            )
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        items(data) { item ->
            Column(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(
                        color = when {
                            item.today -> PointColor
                            item.background -> TabColor
                            selection[0] == year && selection[1] == month && selection[2] == item.day -> TabColor
                            else -> Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .clickable { onClick(data.indexOf(item)) },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = if (item.day == 0) "" else item.day.toString(), style = TextStyle(
                    fontSize = 13.sp,
                    color = if (item.today) Color.White else DefaultFontColor,
                    fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(text = if (item.expense == 0) "" else item.expense.toString(), style = TextStyle(
                    fontSize = 8.sp,
                    color = when {
                        item.expense > 0 && item.today -> Color.White
                        item.expense > 0 -> Color.Black
                        item.expense < 0 -> Color.Red
                        else -> Color.Transparent
                    }
                )
                )
            }
        }
    }
}