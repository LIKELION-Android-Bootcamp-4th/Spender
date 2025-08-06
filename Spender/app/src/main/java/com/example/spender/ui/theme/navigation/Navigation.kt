package com.example.spender.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.spender.MainScreen
import com.example.spender.feature.analysis.AnalysisScreen
import com.example.spender.feature.auth.AuthScreen
import com.example.spender.feature.home.HomeScreen
import com.example.spender.feature.home.ui.NotificationListScreen
import com.example.spender.feature.mypage.MypageScreen
import com.example.spender.feature.mypage.ui.BudgetScreen
import com.example.spender.feature.mypage.ui.ExpenseCategoryScreen
import com.example.spender.feature.mypage.ui.IncomeCategoryScreen
import com.example.spender.feature.mypage.ui.NotificationScreen
import com.example.spender.feature.mypage.ui.RegularExpenseScreen
import com.example.spender.feature.onboarding.OnboardingScreen
import com.example.spender.feature.report.ui.detail.ReportDetailScreen
import com.example.spender.feature.report.ui.list.ReportListScreen

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

        composable(Screen.BudgetScreen.route) {
            BudgetScreen(navController)
        }
        composable(Screen.IncomeCategoryScreen.route) {
            IncomeCategoryScreen(navController)
        }
        composable(Screen.ExpenseCategoryScreen.route) {
            ExpenseCategoryScreen(navController)
        }
        composable(Screen.RegularExpenseScreen.route) {
            RegularExpenseScreen(navController)
        }
        composable(Screen.NotificationScreen.route) {
            NotificationScreen(navController)
        }


    }
}