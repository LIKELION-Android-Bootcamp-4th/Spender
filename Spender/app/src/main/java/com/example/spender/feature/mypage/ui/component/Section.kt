package com.example.spender.feature.mypage.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.LightGray

@Composable
fun Section(
    title: String,
    items: List<MyPageItemType>,
    onItemClick: (MyPageItemType) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 30.dp).padding(top = 20.dp)) {
        Text(title, color = MaterialTheme.colorScheme.onTertiary, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
        items.forEach { item ->
            MyPageItem(item = item, onClick = { onItemClick(item) })
        }
    }
}
