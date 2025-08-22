package com.e1i3.spender.feature.home.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.e1i3.spender.R
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.SelectedColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun TierBadge(
    level: Int,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    pointColor: Color = PointColor,
    elevation: Dp = 2.dp,
    contentDescription: String? = "Tier $level",
    onClick: () -> Unit = {}
) {
    val tierDrawable = tierDrawableRes(level)

    Box(
        modifier = modifier
            .width(size + 50.dp)
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = modifier
                .size(size)
                .shadow(elevation, CircleShape, clip = false)
                .background(pointColor, CircleShape)
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = tierDrawable),
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize(0.72f)
                    .align(Alignment.Center)
                    .offset(x = -(3).dp),
                contentScale = ContentScale.Fit
            )
        }
        Box(
            modifier = Modifier
                .size(75.dp)
                .background(SelectedColor, CircleShape)
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ) {
            Text(contentDescription ?: "", style = Typography.bodyMedium, color = Color.Black)
        }
    }
}

@DrawableRes
fun tierDrawableRes(level: Int): Int = when (level) {
    1 -> R.drawable.tier_1
    2 -> R.drawable.tier_2
    3 -> R.drawable.tier_3
    4 -> R.drawable.tier_4
    5 -> R.drawable.tier_5
    else -> R.drawable.tier_3
}
