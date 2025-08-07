package com.example.spender.feature.home.ui

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.R
import com.example.spender.ui.theme.LightSurface
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.PointRedColor
import com.example.spender.ui.theme.Typography

@Composable
fun BudgeProgress(
    budget: Int,
    totalExpense: Int,
    navHostController: NavHostController
) {
    val percentage = totalExpense.toFloat() / budget.toFloat()
    val percentText = "${(percentage * 100).toInt()}%"
    val highlightColor = if (percentage >= 1f) PointRedColor else PointColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "지금까지 예산의 ",
                style = Typography.titleMedium
            )
            Text(
                text = "$percentText",
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

        BudgetProgressBar(percentage = percentage, percentText = percentText)

        Spacer(Modifier.height(8.dp))

        // 예산 대비 지출 경고 카드
        Card(
            modifier = Modifier
                .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = LightSurface,
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
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "이대로라면 예산을 초과할 것 같아요!", //TODO: percentage 별로 분기할 것
                    style = Typography.bodyMedium
                )
            }
        }

        //예산 설정 하러 가기 텍스트 버튼
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
                    )
                )
            }
        }
    }
}