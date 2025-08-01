package com.example.spender.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.spender.MainScreen
import com.example.spender.feature.analysis.AnalysisScreen
import com.example.spender.feature.home.HomeScreen
import com.example.spender.feature.mypage.MypageScreen
import com.example.spender.feature.report.ui.list.ReportListScreen

@Composable
fun SpenderNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(Screen.MainScreen.route) {
            MainScreen(navController)
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
    }
}