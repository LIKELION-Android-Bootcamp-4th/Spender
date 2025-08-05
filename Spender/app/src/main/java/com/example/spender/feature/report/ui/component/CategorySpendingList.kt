package com.example.spender.feature.report.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.Typography

@Composable
fun CategorySpendingList() {
    val categories = listOf(
        Triple("식비", "186,000원", Color(0xFF18C1AC)),
        Triple("자녀/육아", "64,000원", Color(0xFF2E9AFE)),
        Triple("교통", "43,400원", Color(0xFF8E44AD)),
        Triple("교육/학습", "9,700원", Color(0xFF9B59B6)),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        categories.forEach { (name, amount, dotColor) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(dotColor, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = name, style = Typography.bodyMedium)
                    Text(text = " 61%", style = Typography.bodySmall, color = LightFontColor)
                }

                Text(text = amount, style = Typography.bodyMedium)
            }
        }
    }
}
