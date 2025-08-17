package com.example.spender.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.spender.ui.theme.DarkPointColor
import com.example.spender.ui.theme.DefaultFontColor
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.LightPointColor
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.navigation.BottomNavigationItem
import androidx.compose.material.ripple.rememberRipple as rememberRipple

@Composable
fun BottomNavigationBar(navHostController: NavHostController) {
    val screens = listOf(
        BottomNavigationItem.Home,
        BottomNavigationItem.Analysis,
        BottomNavigationItem.Report,
        BottomNavigationItem.Mypage
    )
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 5.dp,
        actions = {
            screens.take(2).forEach { screen ->
                val isSelected = currentDestination?.route == screen.route
                BottomAppBarItem(
                    icon = screen.icon,
                    label = stringResource(screen.title),
                    selected = isSelected,
                    onClick = { navHostController.navigate(screen.route) }
                )
            }
            Spacer(modifier = Modifier.weight(1f)) // FAB 공간 확보
            screens.takeLast(2).forEach { screen ->
                val isSelected = currentDestination?.route == screen.route
                BottomAppBarItem(
                    icon = screen.icon,
                    label = stringResource(screen.title),
                    selected = isSelected,
                    onClick = { navHostController.navigate(screen.route) }
                )
            }
        }
    )
}

@Composable
fun RowScope.BottomAppBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            tint = if (selected) DarkPointColor else LightPointColor
        )
        Text(
            text = label,
            color = if (selected) DarkPointColor else LightFontColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}