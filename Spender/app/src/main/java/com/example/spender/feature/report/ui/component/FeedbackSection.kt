package com.example.spender.feature.report.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spender.R
import com.example.spender.ui.theme.LightBackgroundColor
import com.example.spender.ui.theme.LightPointColor
import com.example.spender.ui.theme.LightSurface
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography
import kotlin.io.path.Path
import kotlin.io.path.moveTo

@Composable
fun FeedbackSection(feedback: String){
    Column {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            "지출이의 의견",
            style = Typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // TODO : 말풍선 형식으로 (예산 보다 아꼈으면 웃는 지출이 / 더 쓰면 화난 지출이)
        FeedbackBox(
            feedback = feedback,
            imageResId = R.drawable.spender_happy
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FeedbackBox(
    feedback: String,
    imageResId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "지출이 캐릭터",
                modifier = Modifier
                    .size(70.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "지출이",
                style = Typography.bodyMedium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Box(
            modifier = Modifier
                .padding(start = 32.dp, top = 4.dp) // 이미지랑 꼬리 위치 맞추기
        ) {
            TriangleTail(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 4.dp)
                    .offset(x = 2.dp, y = (-7).dp)
            )
            Box(
                modifier = Modifier
                    .padding(start = 4.dp, bottom = 0.dp) // 꼬리 너비만큼 오른쪽으로 밀기
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = feedback,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun TriangleTail(modifier: Modifier = Modifier) {
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    Canvas(
        modifier = modifier
            .size(12.dp)
    ) {
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, 0f) // 왼쪽 위
            lineTo(size.width, size.height / 2f) // 오른쪽 중간
            lineTo(0f, size.height) // 왼쪽 아래
            close()
        }
        drawPath(path = path, color = surfaceVariantColor)
    }
}
