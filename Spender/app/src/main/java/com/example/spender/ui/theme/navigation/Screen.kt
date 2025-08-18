package com.example.spender.ui.theme.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main")
    object AuthScreen : Screen("auth")
    object OnboardingScreen : Screen("onboarding")
    object SplashScreen : Screen("splash")

    object NotificationListScreen: Screen("notification_list")

    object ReportDetail : Screen("report_detail/{month}") {
        fun createRoute(month: String) = "report_detail/$month"
    }
    object ExpenseDetailScreen : Screen("expense_detail/{expenseId}") {
        fun createRoute(expenseId: String) = "expense_detail/$expenseId"
    }
    object IncomeDetailScreen : Screen("income_detail/{incomeId}") {
        fun createRoute(incomeId: String) = "income_detail/$incomeId"
    }
    object RegularExpenseDetailScreen : Screen("regular_expense_detail/{regularExpenseId}") {
        fun createRoute(regularExpenseId: String) = "regular_expense_detail/$regularExpenseId"
    }

    object ExpenseRegistrationScreen : Screen("expense_registration/{selectedTabIndex}") {
        fun createRoute(selectedTabIndex: Int) = "expense_registration/$selectedTabIndex"
    }

    object OcrResultScreen : Screen("ocr_result/{title}/{amount}/{date}") {
        fun createRoute(title: String, amount: String, date: String) =
            "ocr_result/$title/$amount/$date"
    }

    object BudgetScreen : Screen("budget")
    object IncomeCategoryScreen : Screen("income_category")
    object ExpenseCategoryScreen : Screen("expense_category")
    object RegularExpenseScreen : Screen("regular_expense")
    object NotificationScreen : Screen("notification")
    object OpenSourceScreen: Screen("open_source")
    object IncomeRegistrationScreen : Screen("income_registration")

}