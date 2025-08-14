package com.example.spender.feature.mypage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spender.feature.expense.domain.model.RegularExpense
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.TabColor
import com.example.spender.ui.theme.Typography

@Composable
fun RegularExpenseListItem(
    regularExpense: RegularExpense,
    onManageClick: () -> Unit
) {
    Column(modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = regularExpense.title,
                    style = Typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "매월 ${regularExpense.day}일",
                    style = Typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = 14.sp
                    )
                )
            }

            TextButton(
                onClick = onManageClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "관리",
                    style = Typography.labelMedium.copy(
                        color = PointColor
                    )
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier,
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
