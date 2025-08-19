package com.e1i3.spender.feature.mypage.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.e1i3.spender.R
import com.e1i3.spender.feature.mypage.ui.component.CircularImage
import com.e1i3.spender.feature.mypage.ui.component.EditButton
import com.e1i3.spender.ui.theme.Typography

@Composable
fun ProfileSection() {
    Column(
        modifier = Modifier
    ) {
        Text(
            text = "프로필",
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
                text = "다른 사람들에게 보이는 정보예요!",
                style = Typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularImage(profileUrl = null)

            Spacer(Modifier.height(16.dp))

            Row {
                EditButton(text = "변경", onClick = {}) // TODO: 카메라/갤러리 다이얼로그
                Spacer(Modifier.width(8.dp))
                EditButton(text = "삭제", onClick = {}) // TODO: 기존의 profileUrl을 삭제하고 기본 아이콘으로 변경
            }
        }

        Spacer(Modifier.height(64.dp))

        Row {
            Text(
                text = "닉네임",
                style = Typography.titleMedium
            )
            Spacer(Modifier.width(16.dp))

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "닉네임 들어갈 곳 추후 변경",
                        style = Typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {}, //TODO: 수정 다이얼로그
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "닉네임 수정",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.tertiary,
                    thickness = 1.dp,
                )
            }
        }
    }
}
