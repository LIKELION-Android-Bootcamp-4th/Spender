package com.example.spender.feature.report.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spender.R
import com.example.spender.ui.theme.Typography

@Composable
fun FeedbackSection(feedback: String){
    Column {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            "지출이의 의견",
            style = Typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // TODO : 말풍선 형식으로 (예산 보다 아꼈으면 웃는 지출이 / 더 쓰면 화난 지출이)
        FeedbackBox(
            feedback = feedback,
            imageResId = R.drawable.spender_happy
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FeedbackBox(
    feedback: String,
    imageResId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 피드백
        Text(
            text = feedback,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            style = Typography.bodyMedium
        )

        // 지출이
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "캐릭터",
            modifier = Modifier
                .size(64.dp)
        )
    }
}
