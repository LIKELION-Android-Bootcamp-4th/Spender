package com.e1i3.spender.feature.report.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.e1i3.spender.feature.report.ui.model.CategoryUiModel
import com.e1i3.spender.ui.theme.Typography

@Composable
fun CategorySpendingSection(
    categories : List<CategoryUiModel>
){
    val labels = categories.map { it.label }
    val values = categories.map { it.percentage }
    val colors = categories.map { it.color.toArgb() }

    val maxCategory = categories.maxByOrNull { it.percentage }

    if (categories.isEmpty()) {
        Text("표시할 카테고리 지출 정보가 없습니다.")
        return
    }

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
//                withStyle(SpanStyle(Color(0xFFFECD43))){
                withStyle(SpanStyle(color = maxCategory?.color ?: Color(0xFFFECD43),
                    fontSize = 18.sp)){
                    append(maxCategory?.label ?: "기타")
                }
                append("이에요")
            },
            style = Typography.titleSmall,
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        CategoryPieChart(
            labels = labels,
            values = values,
            colors = colors
        )

        Spacer(modifier = Modifier.height(24.dp))

        CategorySpendingList(categories)
    }
}