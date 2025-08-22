package com.e1i3.spender.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SafeArea(
    modifier: Modifier = Modifier,
    insets: WindowInsets = WindowInsets.systemBars,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.padding(insets.asPaddingValues()),
        content = content
    )
}