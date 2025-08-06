package com.example.spender.feature.report.ui.component

import android.util.Log
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
import com.example.spender.ui.theme.Typography
import androidx.core.graphics.toColorInt
import com.example.spender.feature.report.ui.model.EmotionUiModel

@Composable
fun EmotionTagSection(
    emotions: List<EmotionUiModel>
){
    val labels = emotions.map { it.label }
    val values = emotions.map { it.percentage }
    var colors = emotions.map { it.color.toArgb() }

    Log.d("EmotionBarChart", "labels=$labels, values=$values, colors=$colors")


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
