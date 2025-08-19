package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.e1i3.spender.R
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun BubbleWithText(
    percentText: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = PointColor
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.messagebubble),
            contentDescription = null,
//            tint = Color.Unspecified,
            tint = backgroundColor,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = percentText,
            color = Color.White,
            style = Typography.titleSmall.copy(
                color = Color.White,
                fontSize = 14.sp
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}