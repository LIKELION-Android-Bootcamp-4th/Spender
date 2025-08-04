package com.example.spender.feature.report.ui.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.Typography
import androidx.core.graphics.toColorInt

@Composable
fun EmotionTagSection(){
    val labels = listOf("만족", "불만", "충동", "억울")
    val values = listOf(26f, 8f, 12f, 16f)
    var colors = listOf(
        "#A566FF".toColorInt(), // 만족: 보라
        "#FFE761".toColorInt(), // 불만: 노랑
        "#FF7A6A".toColorInt(), // 충동: 주황
        "#36CCB3".toColorInt()  // 억울: 초록
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ){

        Text(
            "감정태그 비율",
            style = Typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        EmotionBarChart(
            labels = labels,
            values = values,
            colors = colors
        )
    }
}
