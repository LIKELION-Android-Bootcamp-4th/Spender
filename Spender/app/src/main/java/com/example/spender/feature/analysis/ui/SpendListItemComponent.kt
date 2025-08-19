package com.example.spender.feature.analysis.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import com.example.spender.core.common.util.toCurrency
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.PointRedColor
import com.example.spender.ui.theme.Typography

@Composable
fun SpendListItemComponent(item: ExpenseDto?, onClick: () -> Unit) {
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
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item?.title ?: "",
                style = Typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Row {
                if ((item?.amount?.toInt() ?: 0) < 0) {
                    Text(
                        text = "- ${Math.abs(item?.amount ?: 0).toCurrency()}",
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