package com.example.spender.feature.analysis.ui

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spender.feature.analysis.domain.model.SpendListItemData
import com.example.spender.ui.theme.NotoSansFamily
import com.example.spender.ui.theme.Typography

@Composable
fun SpendListItemComponent(item: SpendListItemData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 15.dp)
    ) {
        Row {
            Text(item.name, style = Typography.bodyMedium)
            Spacer(Modifier.weight(1f))
            Text(
                text = if (item.income) "+${DecimalFormat("#,###").format(item.price)}원" else "-${DecimalFormat("#,###").format(item.price)}원",
                style = TextStyle(
                    color = if (item.income) Color.Blue else Color.Red,
                    fontFamily = NotoSansFamily,
                    fontSize = 16.sp
                )
            )
        }
    }
}