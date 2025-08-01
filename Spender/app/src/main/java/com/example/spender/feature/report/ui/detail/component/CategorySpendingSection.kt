package com.example.spender.feature.report.ui.detail.component

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color as AndroidColor

@Composable
fun CategorySpendingSection(){
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

        CategoryPieChart()

        Spacer(modifier = Modifier.height(24.dp))

        CategorySpendingList()


    }
}