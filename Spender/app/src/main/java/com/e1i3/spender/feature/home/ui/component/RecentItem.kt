package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.e1i3.spender.core.common.util.toCurrency
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecentItem(
    title: String,
    amount: Int,
    type: String,
    date: Date,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f),) {
                Text(
                    text = title,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val dateFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
                Text(
                    text = dateFormat.format(date),
                    style = Typography.bodySmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
            Row(verticalAlignment = Alignment.Bottom) {
                val amountText = if (type == "INCOME") {
                    "+ ${amount.toCurrency()}"
                } else {
                    "- ${amount.toCurrency()}"
                }
                val amountColor = if (type == "INCOME") {
                    PointColor
                } else {
                    MaterialTheme.colorScheme.error
                }

                Text(
                    text = amountText,
                    style = Typography.titleSmall,
                    color = amountColor
                )
                Text(
                    text = " 원",
                    style = Typography.titleSmall,
                    color = amountColor
                )
            }
        }
    }
}