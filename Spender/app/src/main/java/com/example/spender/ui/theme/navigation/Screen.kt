package com.example.spender.ui.theme.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main")
    object AuthScreen : Screen("auth")
    object OnboardingScreen : Screen("onboarding")

    object NotificationListScreen: Screen("notification_list")

    object ReportDetail : Screen("report_detail/{month}") {
        fun createRoute(month: String) = "report_detail/$month"
    }
    object ExpenseDetailScreen : Screen("expense_detail/{expenseId}") {
        fun createRoute(expenseId: String) = "expense_detail/$expenseId"
    }

    object BudgetScreen : Screen("budget")
    object IncomeCategoryScreen : Screen("income_category")
    object ExpenseCategoryScreen : Screen("expense_category")
    object RegularExpenseScreen : Screen("regular_expense")
    object NotificationScreen : Screen("notification")
    object ExpenseRegistrationScreen : Screen("expense_registration")
    object IncomeRegistrationScreen : Screen("income_registration")

}