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

data class TierInfo(
    @DrawableRes val mainDrawableRes: Int,
    @DrawableRes val subDrawableRes: Int,
    val backgroundColor: Color
)

fun getTierInfo(level: Int): TierInfo = when (level) {
    1 -> TierInfo(R.drawable.tier_1, R.drawable.rank_1, Color(0xFF5A5A5A))
    2 -> TierInfo(R.drawable.tier_2, R.drawable.rank_2, Color(0xFFCD7F32))
    3 -> TierInfo(R.drawable.tier_3, R.drawable.rank_3, Color(0xFFFAEFE6))
    4 -> TierInfo(R.drawable.tier_4, R.drawable.rank_4, Color(0xFF3182F6))
    5 -> TierInfo(R.drawable.tier_5, R.drawable.rank_5, Color(0xFFFFE46C))
    else -> TierInfo(R.drawable.tier_3, R.drawable.rank_3, Color(0xFFFAEFE6))
}

@Composable
fun TierBadge(
    level: Int,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    elevation: Dp = 2.dp,
    contentDescription: String? = "Tier $level",
    onClick: () -> Unit = {}
) {
    val tierInfo = getTierInfo(level)

    Box(
        modifier = modifier
            .width(size + 50.dp)
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = modifier
                .size(size)
                .shadow(elevation, CircleShape, clip = false)
                .background(tierInfo.backgroundColor, CircleShape)
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = tierInfo.mainDrawableRes),
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize(0.79f)
                    .align(Alignment.Center)
                    .offset(x = -(3).dp),
                contentScale = ContentScale.Fit
            )
        }
        Box(
            modifier = Modifier
                .size(85.dp)
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = tierInfo.subDrawableRes),
                contentDescription = contentDescription,
                modifier = Modifier.size(100.dp)
            )
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
