package com.e1i3.spender.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.e1i3.spender.R
import com.e1i3.spender.core.common.util.toCurrency
import com.e1i3.spender.feature.home.ui.component.BudgetProgressBar
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.PointRedColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun BudgeProgress(
    percentage: Float,
    nickname: String = "",
    navHostController: NavHostController,
    showSetBudgetButton: Boolean = true,
    showNickname: Boolean = false,
) {
    val percentageForDisplay = percentage
    val percentageForProgress = percentage / 100f
    val percentText = "${percentageForDisplay.toInt()}%"
    val highlightColor = if (percentageForProgress >= 1f) PointRedColor else PointColor

    val warningText = when{
        percentageForDisplay <= 60f -> stringResource(R.string.budgets_green)
        percentageForDisplay <= 99f -> stringResource(R.string.budgets_yellow)
        else -> stringResource(R.string.budgets_red)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        if(showNickname){
            Text("${nickname}님은", style = Typography.titleMedium)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "지금까지 예산의 ",
                style = Typography.titleMedium
            )
            Text(
                text = "${percentageForDisplay.toInt().toCurrency()}",
                style = Typography.titleMedium.copy(
                    color = highlightColor
                )
            )
            Text(
                text = "를 썼어요!",
                style = Typography.titleMedium
            )
        }

        Spacer(Modifier.height(8.dp))

        BudgetProgressBar(
            percentage = percentageForProgress,
            percentText = percentText
        )

        Spacer(Modifier.height(8.dp))

        // 예산 대비 지출 경고 카드
        Card(
            modifier = Modifier
                .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.warning_icon),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = warningText,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        //예산 설정 하러 가기 텍스트 버튼
        if(showSetBudgetButton){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier,
                    onClick = {
                        navHostController.navigate("budget")
                    }
                ) {
                    Text(
                        text = "예산 설정하러 가기",
                        style = Typography.labelMedium.copy(
                            textDecoration = TextDecoration.Underline
                        ),
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    }
}