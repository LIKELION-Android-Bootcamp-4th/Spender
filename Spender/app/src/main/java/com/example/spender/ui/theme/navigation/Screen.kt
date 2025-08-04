package com.example.spender.ui.theme.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main")
    object AuthScreen : Screen("auth")
    object OnboardingScreen : Screen("onboarding")

    object ReportDetail : Screen("report_detail/{reportId}") {
        fun createRoute(reportId: Int) = "report_detail/$reportId"
    }
}