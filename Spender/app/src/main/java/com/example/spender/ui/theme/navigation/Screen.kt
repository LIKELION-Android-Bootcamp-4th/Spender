package com.example.spender.ui.theme.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main")
    object OnboardingScreen : Screen("onboarding")

    object ReportDetail : Screen("report_detail/{reportId}") {
        fun createRoute(reportId: Int) = "report_detail/$reportId"
    }

    object BudgetScreen : Screen("budget")
    object IncomeCategoryScreen : Screen("income_category")
    object ExpenseCategoryScreen : Screen("expense_category")
    object RegularExpenseScreen : Screen("regular_expense")
    object NotificationScreen : Screen("notification")

}