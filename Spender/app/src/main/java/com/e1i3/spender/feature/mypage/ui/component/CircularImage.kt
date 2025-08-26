package com.e1i3.spender.feature.mypage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.e1i3.spender.R
import com.e1i3.spender.ui.theme.LightGray
import com.e1i3.spender.ui.theme.TabColor
import com.e1i3.spender.ui.theme.WhiteColor

@Composable
fun CircularImage(
    profileUrl: String?,
    size: Dp = 160.dp,
) {
    Card(
        modifier = Modifier
            .size(size)
            .shadow(elevation = 3.dp, shape = CircleShape, clip = false)
            .background(MaterialTheme.colorScheme.surface , CircleShape)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = CircleShape),
        colors = CardDefaults.cardColors(
            containerColor = WhiteColor
        )
    ) {
        if (profileUrl.isNullOrEmpty() || profileUrl.isBlank()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.default_user_icon),
                    contentDescription = "기본 프로필 아이콘",
                    modifier = Modifier.size(size),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(TabColor)

                )
            }
        } else {
            AsyncImage(
                model = profileUrl,
                contentDescription = "사용자 프로필 이미지",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
    }
}