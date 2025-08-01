package com.example.spender.feature.report.ui.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

@Composable
fun BudgetProgressSection(
    progressPercent: Int = 60
){
    Column {
        Spacer(modifier = Modifier.height(24.dp))

        // 예산 퍼센트
        Text(
            buildAnnotatedString {
                append("예산의 ")
                withStyle(SpanStyle(PointColor)){
                    append("${progressPercent}%")
                }
                append("를 썼어요")
            },
            style = Typography.titleMedium
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 상태바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 0.dp)
        ) {
            LinearProgressIndicator(
                progress = { progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF3182F6),
                trackColor = Color.LightGray,
            )

            // 퍼센트 텍스트 위에 띄우기
            Text(
                text = "$progressPercent",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(Color(0xFF3182F6), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = Typography.titleSmall
            )
        }

    }
}