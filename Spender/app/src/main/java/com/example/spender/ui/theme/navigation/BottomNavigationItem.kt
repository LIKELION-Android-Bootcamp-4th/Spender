package com.example.spender.ui.theme.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.spender.R

sealed class BottomNavigationItem(
    val route: String,
    val title: Int,
    val icon: ImageVector
) {
    data object Home: BottomNavigationItem(
        route = "home",
        title = R.string.tab_home,
        icon = Icons.Default.Home
    )
    data object Analysis: BottomNavigationItem(
        route = "analysis",
        title = R.string.tab_analysis,
        icon = Icons.AutoMirrored.Filled.ShowChart
    )
    data object Report: BottomNavigationItem(
        route = "report",
        title = R.string.tab_report,
        icon = Icons.AutoMirrored.Filled.Assignment
    )
    data object Mypage: BottomNavigationItem (
        route = "mypage",
        title = R.string.tab_mypage,
        icon = Icons.Default.Person
    )
}