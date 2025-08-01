package com.example.spender.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.LightSurface
import com.example.spender.ui.theme.Typography

@Composable
fun RecentItem(
    //TODO: 각 데이터의 필드(수입,지출 제목 & 금액) 넘겨받기
) {
    Card(
        modifier = Modifier
            .height(72.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "지출/수입 제목의 자리",
                style = Typography.bodyMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "+- 000,000",
                    style = Typography.titleSmall.copy(
                        // TODO: 수입/지출에 따라 색 분기
                    )
                )
                Text(
                    text = " 원",
                    style = Typography.bodyMedium.copy(
                        // TODO: 수입/지출에 따라 색 분기
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun RecentItemPreview() {
    RecentItem()
}