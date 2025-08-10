package com.example.spender.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.spender.MainScreen
import com.example.spender.feature.analysis.AnalysisScreen
import com.example.spender.feature.auth.AuthScreen
import com.example.spender.feature.expense.ui.ExpenseRegistrationParentScreen
import com.example.spender.feature.expense.ui.expensedetail.ExpenseDetailScreen
import com.example.spender.feature.expense.ui.recurringexpensedetail.RecurringExpenseDetailScreen
import com.example.spender.feature.home.HomeScreen
import com.example.spender.feature.home.ui.NotificationListScreen
import com.example.spender.feature.income.ui.IncomeRegistrationScreen
import com.example.spender.feature.income.ui.incomedetail.IncomeDetailScreen
import com.example.spender.feature.mypage.MypageScreen
import com.example.spender.feature.mypage.ui.BudgetScreen
import com.example.spender.feature.mypage.ui.ExpenseCategoryScreen
import com.example.spender.feature.mypage.ui.IncomeCategoryScreen
import com.example.spender.feature.mypage.ui.NotificationScreen
import com.example.spender.feature.mypage.ui.RegularExpenseScreen
import com.example.spender.feature.onboarding.OnboardingScreen
import com.example.spender.feature.report.ui.detail.ReportDetailScreen
import com.example.spender.feature.report.ui.list.ReportListScreen
import com.example.spender.feature.splash.SplashScreen

@Composable
fun SpenderNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.MainScreen.route) {
            MainScreen(navController)
        }
        composable(Screen.OnboardingScreen.route) {
            OnboardingScreen(navController)
        }
        composable(Screen.AuthScreen.route) {
            AuthScreen(navController)
        }
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(BottomNavigationItem.Home.route) {
            HomeScreen(navController)
        }
        composable(BottomNavigationItem.Analysis.route) {
            AnalysisScreen(navController)
        }
        composable(BottomNavigationItem.Report.route) {
            ReportListScreen(navController)
        }
        composable(BottomNavigationItem.Mypage.route) {
            MypageScreen(navController)
        }

        composable(Screen.NotificationListScreen.route) {
            NotificationListScreen(navController)
        }

        composable(Screen.ReportDetail.route) { backStackEntry ->
            val month = backStackEntry.arguments?.getString("month")
            if (month != null) {
                ReportDetailScreen(navController, month)
            }
        }
        composable(Screen.ExpenseDetailScreen.route) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId")
            if (expenseId != null) {
                ExpenseDetailScreen(navController, expenseId = expenseId)
            }
        }
        composable(Screen.IncomeDetailScreen.route) { backStackEntry ->
            val incomeId = backStackEntry.arguments?.getString("incomeId")
            if (incomeId != null) {
                IncomeDetailScreen(navController , incomeId = incomeId)
            }
        }
        composable(Screen.RegularExpenseDetailScreen.route) { backStackEntry ->
            val regularExpenseId = backStackEntry.arguments?.getString("regularExpenseId")
            if (regularExpenseId != null) {
                RecurringExpenseDetailScreen(navController , regularExpenseId = regularExpenseId)
            }
        }
        composable(
            Screen.ExpenseRegistrationScreen.route,
            listOf(navArgument("selectedTabIndex") {
                type = NavType.IntType
                defaultValue = 1
            })
        ) { backStackEntry ->
            val selectedTabIndex = backStackEntry.arguments?.getInt("selectedTabIndex")
            ExpenseRegistrationParentScreen(
                navController,
                selectedTabIndex ?: 1
            )
        }

        composable(Screen.BudgetScreen.route) {
            BudgetScreen(navController)
        }
        composable(Screen.IncomeCategoryScreen.route) {
            IncomeCategoryScreen(navController)
        }
        composable(Screen.ExpenseCategoryScreen.route) {
            ExpenseCategoryScreen(navController)
        }
        composable(Screen.IncomeCategoryScreen.route) {
            IncomeCategoryScreen(navController)
        }
        composable(Screen.RegularExpenseScreen.route) {
            RegularExpenseScreen(navController)
        }
        composable(Screen.NotificationScreen.route) {
            NotificationScreen(navController)
        }
//        composable(Screen.ExpenseRegistrationScreen.route) {
//            ExpenseRegistrationParentScreen(navController)
//        }
        composable(Screen.IncomeRegistrationScreen.route) {
            IncomeRegistrationScreen(navController)
        }
    }
}