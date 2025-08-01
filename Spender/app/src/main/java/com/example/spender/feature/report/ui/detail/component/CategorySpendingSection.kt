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
        CategoryPieChart()

        Spacer(modifier = Modifier.height(16.dp))

        CategorySpendingList()


    }
}