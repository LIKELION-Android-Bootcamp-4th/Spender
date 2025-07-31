package com.example.spender.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
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

    NavigationBar (
        containerColor = MaterialTheme.colorScheme.surface
    ){
        screens.forEach { screen ->
            val isSelected = currentDestination?.route == screen.route

            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(text = stringResource(screen.title), color = if (isSelected) PointColor else Color.Gray) },
                selected = isSelected,
                onClick = { navHostController.navigate(screen.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PointColor,
                    selectedTextColor = PointColor,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}