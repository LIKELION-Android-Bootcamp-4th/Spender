package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.e1i3.spender.ui.theme.LightFontColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun YearSelectorRow(
    year: Int,
    currentYear: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onYearClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 60.dp, vertical = 8.dp)
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onPrev() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${year - 1}년",
                color = LightFontColor,
                style = Typography.titleSmall
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onYearClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${year}년",
                style = Typography.titleMedium,
                textDecoration = TextDecoration.Underline
            )
        }

        if (year < currentYear) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNext() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${year + 1}년",
                    color = LightFontColor,
                    style = Typography.titleSmall
                )
            }
        } else {
            Box(modifier = Modifier.weight(1f))
        }
    }
}