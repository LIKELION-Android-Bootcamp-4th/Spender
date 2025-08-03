package com.example.spender.feature.report.ui.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.spender.ui.theme.Typography

@Composable
fun CategorySpendingSection(){
    val labels = listOf("식비", "자녀/육아", "교통", "교육/학습")
    val values = listOf(61f, 21f, 14f, 3f)
    val colors = listOf(
        "#18C1AC".toColorInt(), // 식비
        "#2E9AFE".toColorInt(), // 자녀/육아
        "#6378EC".toColorInt(), // 교통
        "#9B59B6".toColorInt()  // 교육/학습
    )

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ){
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "카테고리별 지출",
            style = Typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            buildAnnotatedString {
                append("이번달 가장 돈을 많이 쓴 카테고리는\n")
                withStyle(SpanStyle(Color(0xFFFECD43))){
                    append("식품")
                }
                append("이에요")
            },
            style = Typography.titleSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        CategoryPieChart(
            labels = labels,
            values = values,
            colors = colors
        )

        Spacer(modifier = Modifier.height(24.dp))

        CategorySpendingList()


    }
}