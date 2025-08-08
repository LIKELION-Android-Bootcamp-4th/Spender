package com.example.spender.core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.PointColor

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = true
) {
    if (isFullScreen) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PointColor)
        }
    } else {
        CircularProgressIndicator(
            modifier = modifier
                .size(32.dp),
            strokeWidth = 4.dp,
            color = PointColor
        )
    }
}
