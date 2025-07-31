package com.example.spender.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.LightSurface
import com.example.spender.ui.theme.PointColor

@Composable
fun BudgetProgressBar(
    modifier: Modifier = Modifier,
    budget: Int,
    totalExpense: Int
) {
    val percentage = totalExpense.toFloat() / budget.toFloat()
    val percentText = "${(percentage * 100).toInt()}%"

    Card(
        modifier = modifier
            .height(108.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface
        ),
    ) {
        //TODO : 텍스트말풍선이 프로그래스 바를 쫓아가게 수정할 것
        BubbleWithText(percentText)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color(0xFFF0F0F0))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage.coerceIn(0f, 1f))
                    .height(16.dp)
                    .clip(RoundedCornerShape(100))
                    .background(PointColor)
            )
        }
    }
}