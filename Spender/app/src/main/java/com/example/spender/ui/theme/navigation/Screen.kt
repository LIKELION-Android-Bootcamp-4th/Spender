package com.example.spender.ui.theme.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main")
}