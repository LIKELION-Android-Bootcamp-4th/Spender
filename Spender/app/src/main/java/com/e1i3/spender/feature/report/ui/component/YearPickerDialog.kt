package com.e1i3.spender.feature.report.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun YearPickerDialog(
    currentYear: Int,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "연도 선택",
                    style = Typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val yearList = (currentYear downTo currentYear - 3).toList()
                var tempYear by remember { mutableIntStateOf(selectedYear) }

                LazyColumn(
                    modifier = Modifier
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                ) {
                    items(yearList) { year ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    tempYear = year
                                    onYearSelected(year)
                                    onDismiss()
                                }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$year 년",
                                style = Typography.bodyLarge.copy(
                                    fontWeight = if (year == tempYear) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (year == tempYear) PointColor else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

