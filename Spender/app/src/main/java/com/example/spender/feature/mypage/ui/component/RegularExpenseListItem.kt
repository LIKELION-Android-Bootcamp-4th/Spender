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
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.TabColor
import com.example.spender.ui.theme.Typography

//TODO: 인자로 각 Regular_Expense 데이터 받아와서 Text 부분 수정
@Composable
fun RegularExpenseListItem(
    title: String,
    date: String
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
                    text = title,
                    style = Typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = date,
                    style = Typography.titleSmall.copy(
                        color = LightFontColor,
                        fontSize = 14.sp
                    )
                )
            }

            TextButton(
                onClick = {
                    //TODO: 정기지출 관리(수정)화면으로 이동
                },
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
            color = TabColor
        )
    }
}
