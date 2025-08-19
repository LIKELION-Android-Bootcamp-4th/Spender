package com.example.spender.feature.report.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.spender.feature.report.ui.model.EmotionUiModel
import com.example.spender.ui.theme.Typography

@Composable
fun EmotionTagSection(
    emotions: List<EmotionUiModel>
){
    val labels = emotions.map { it.label }
    val values = emotions.map { it.percentage }
    val colors = emotions.map { it.color.toArgb() }

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
