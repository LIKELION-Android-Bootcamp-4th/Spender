package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.e1i3.spender.R
import com.e1i3.spender.ui.theme.LightPointColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    hasUnread: Boolean,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.height(60.dp),
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "프로필 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
            )
        },
        title = { },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    modifier = Modifier.size(28.dp),
                    tint = LightPointColor
                )
            }

            IconButton(onClick = onNotificationClick) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = "알림",
                        modifier = Modifier.size(28.dp),
                        tint = LightPointColor
                    )
                    if (hasUnread) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .offset(x = 2.dp, y = 0.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}