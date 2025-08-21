package com.e1i3.spender.feature.home.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.e1i3.spender.R
import com.e1i3.spender.ui.theme.PointColor

@Composable
fun TierBadge(
    level: Int,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    pointColor: Color = PointColor,
    elevation: Dp = 2.dp,
    contentDescription: String? = "Tier $level"
) {
    val tierDrawable = tierDrawableRes(level)

    Box(
        modifier = modifier
            .size(size)
            .shadow(elevation, CircleShape, clip = false)
            .clip(CircleShape)
            .background(pointColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = tierDrawable),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize(0.72f),
            contentScale = ContentScale.Fit
        )
    }
}

@DrawableRes
private fun tierDrawableRes(level: Int): Int = when (level) {
    1 -> R.drawable.tier_1
    2 -> R.drawable.tier_2
    3 -> R.drawable.tier_3
    4 -> R.drawable.tier_4
    5 -> R.drawable.tier_5
    else -> R.drawable.tier_3
}
