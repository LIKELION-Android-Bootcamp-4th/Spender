package com.e1i3.spender.feature.mypage.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.e1i3.spender.R
import com.e1i3.spender.ui.theme.Typography

@Composable
fun AccountInfoSection(userName: String, userEmail: String, iconRes: Int?) {
    val name = userName
    val email = userEmail
    val iconRes = iconRes

    Column(
        modifier = Modifier
    ) {
        Text(
            text = "계정 정보",
            color = MaterialTheme.colorScheme.onBackground,
            style = Typography.titleSmall,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.warning_icon),
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = MaterialTheme.colorScheme.onTertiary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "이 부분은 수정할 수 없어요.",
                style = Typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

        Spacer(Modifier.height(32.dp))

        Row {
            Text(
                text = "   이름",
                style = Typography.titleMedium
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = name,
                style = Typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(32.dp))

        Row {
            Text(
                text = "이메일",
                style = Typography.titleMedium
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = email,
                style = Typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = "소셜 로그인 아이콘",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}