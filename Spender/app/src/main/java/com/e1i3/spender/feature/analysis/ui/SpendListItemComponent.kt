package com.e1i3.spender.feature.analysis.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.e1i3.spender.core.data.remote.expense.ExpenseDto
import com.e1i3.spender.feature.analysis.mapper.emotionIdToString
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.PointRedColor
import com.e1i3.spender.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@Composable
fun SpendListItemComponent(item: ExpenseDto?, onClick: () -> Unit) {
    val emotion = emotionIdToString(item?.emotionId ?: "")

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
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f),) {
                Text(
                    text = item?.title ?: "",
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (emotion.isNotEmpty()) {
                    Text(
                        text = emotionIdToString(item?.emotionId ?: ""),
                        style = Typography.bodySmall,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Row {
                if ((item?.amount ?: 0) < 0) {
                    Text(
                        text = "- ${abs(item?.amount ?: 0).toCurrency()}",
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
                } else {
                    Text(
                        text = "+ ${item?.amount?.toCurrency()}",
                        style = Typography.titleSmall.copy(
                            color = PointColor
                        )
                    )
                    Text(
                        text = " 원",
                        style = Typography.titleSmall.copy(
                            color = PointColor
                        )
                    )
                }
            }
        }
    }
}