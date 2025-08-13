package com.example.spender.feature.home.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spender.core.common.util.toCurrency
import com.example.spender.ui.theme.LightSurface
import com.example.spender.ui.theme.PointRedColor
import com.example.spender.ui.theme.Typography

@Composable
fun RecentItem(
    title: String,
    amount: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = Typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Row {
                Text(
                    text = "- ${amount.toCurrency()}",
                    style = Typography.titleSmall.copy(
                        color = PointRedColor
                    )
                )
                Text(
                    text = " 원",
                    style = Typography.titleSmall.copy(
                        color = PointRedColor
                    )
                )
            }
        }
    }
}