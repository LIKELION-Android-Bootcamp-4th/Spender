package com.example.spender.feature.report.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

@Composable
fun TotalSpendingSection(
    totalSpending: Int = 988_100 // 서버 연동 전까지 기본값
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PointColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "이번 달 총 지출 %,d원".format(totalSpending),
            style = Typography.titleSmall
        )
    }
}