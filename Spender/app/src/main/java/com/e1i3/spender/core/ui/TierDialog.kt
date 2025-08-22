package com.e1i3.spender.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.e1i3.spender.R
import com.e1i3.spender.ui.theme.Typography

@Composable
fun TierDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .height(500.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "티어제?",
                        style = Typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    defaultDes("ㆍ지출이는 예산 습관을 눈에 보이기 하기 위하여 티어제를 도입하고 있어요!")
                    defaultDes("ㆍ티어는 매 월마다 예산대비 지출을 기준으로 결정하고 있습니다.")

                    Spacer(Modifier.height(16.dp))

                    TierDescription(
                        tierIcon = painterResource(R.drawable.tier_1),
                        tierName = "초보 지출이",
                        tierDes = "이제 막 시작한 단계예요!"
                    )
                    TierDescription(
                        tierIcon = painterResource(R.drawable.tier_2),
                        tierName = "소박 지출이",
                        tierDes = "조금씩 예산을 지키기 시작한 단계예요!"
                    )
                    TierDescription(
                        tierIcon = painterResource(R.drawable.tier_3),
                        tierName = "알뜰 지출이",
                        tierDes = "[기본 티어] 꾸준히 예산을 지킬 준비가 된 상태예요!"
                    )
                    TierDescription(
                        tierIcon = painterResource(R.drawable.tier_4),
                        tierName = "풍족 지출이",
                        tierDes = "예산을 지키며 여유있는 생활중인 단계예요!"
                    )
                    TierDescription(
                        tierIcon = painterResource(R.drawable.tier_5),
                        tierName = "갓생 지출이",
                        tierDes = "예산관리의 천재! \n절약과 소비 밸런스를 완벽히 잡은, \n재정적으로 완전한 상태예요!"
                    )


                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "티어는 어떻게 변하나요?",
                        style = Typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    defaultDes("ㆍ가입 즉시 알뜰 지출이로 시작해요!")
                    defaultDes("ㆍ예산을 어기면 다음 달에 하위 티어로 강등")
                    defaultDes("ㆍ예산을 지키면 다음 달에 상위 티어로 승급")
                    defaultDes("ㆍ풍족 티어를 3개월 연속 유지하면 갓생 지출이로 승급할 수 있어요!")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onDismiss() }) {
                    Text(
                        text = "닫기",
                        style = Typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun defaultDes(
    content: String
) {
    Text(
        text = content,
        style = Typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}


@Composable
fun TierDescription(
    tierIcon: Painter,
    tierName: String,
    tierDes: String
) {
    Row {
        Column {
            Image(
                painter = tierIcon,
                contentDescription = "티어 아이콘",
                Modifier.size(28.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = tierName,
                style = Typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = tierDes,
                style = Typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}


@Preview
@Composable
fun TierDialogPreview() {
    TierDialog(onDismiss = {})
}