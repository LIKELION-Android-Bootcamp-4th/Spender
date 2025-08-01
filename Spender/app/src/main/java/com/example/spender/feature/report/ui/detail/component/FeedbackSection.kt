package com.example.spender.feature.report.ui.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.Typography

@Composable
fun FeedbackSection(){
    Column {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "지출이의 의견",
            style = Typography.titleMedium
        )

    }
}