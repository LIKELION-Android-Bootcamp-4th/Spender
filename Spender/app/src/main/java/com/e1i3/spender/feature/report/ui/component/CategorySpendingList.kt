package com.e1i3.spender.feature.report.ui.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.e1i3.spender.core.common.util.toCurrency
import com.e1i3.spender.feature.report.ui.model.CategoryUiModel
import com.e1i3.spender.ui.theme.Typography

@Composable
fun CategorySpendingList(categories : List<CategoryUiModel>) {

    val sortedCategories = categories.sortedByDescending { it.percentage }

    Column(modifier = Modifier.fillMaxWidth()) {
        sortedCategories.forEach { category ->
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
                            .background(category.color, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = category.label, style = Typography.bodyMedium)
                    Text(text = "   ${category.percentage.toInt()}%", style = Typography.bodySmall, color = MaterialTheme.colorScheme.onTertiary)
                }

                Text(text = "${category.amount.toCurrency()} 원", style = Typography.bodyMedium)
            }
        }
    }
}
