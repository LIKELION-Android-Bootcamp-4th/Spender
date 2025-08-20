package com.e1i3.spender.ui.theme.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.e1i3.spender.MainActivity
import com.e1i3.spender.MainScreen
import com.e1i3.spender.feature.analysis.AnalysisScreen
import com.e1i3.spender.feature.auth.AuthScreen
import com.e1i3.spender.feature.expense.ui.ExpenseRegistrationParentScreen
import com.e1i3.spender.feature.expense.ui.expensedetail.ExpenseDetailScreen
import com.e1i3.spender.feature.expense.ui.ocrresult.OcrResultScreen
import com.e1i3.spender.feature.expense.ui.recurringexpensedetail.RecurringExpenseDetailScreen
import com.e1i3.spender.feature.home.HomeScreen
import com.e1i3.spender.feature.home.ui.NotificationListScreen
import com.e1i3.spender.feature.home.ui.SearchScreen
import com.e1i3.spender.feature.income.ui.IncomeRegistrationScreen
import com.e1i3.spender.feature.income.ui.incomedetail.IncomeDetailScreen
import com.e1i3.spender.feature.mypage.MypageScreen
import com.e1i3.spender.feature.mypage.ui.BudgetScreen
import com.e1i3.spender.feature.mypage.ui.ExpenseCategoryScreen
import com.e1i3.spender.feature.mypage.ui.FriendAddScreen
import com.e1i3.spender.feature.mypage.ui.IncomeCategoryScreen
import com.e1i3.spender.feature.mypage.ui.MyinfoScreen
import com.e1i3.spender.feature.mypage.ui.NotificationScreen
import com.e1i3.spender.feature.mypage.ui.OpenSourceScreen
import com.e1i3.spender.feature.mypage.ui.RegularExpenseScreen
import com.e1i3.spender.feature.onboarding.OnboardingScreen
import com.e1i3.spender.feature.report.ui.detail.ReportDetailScreen
import com.e1i3.spender.feature.report.ui.list.ReportListScreen
import com.e1i3.spender.feature.splash.SplashScreen

@Composable
fun SpenderNavigation(
    navController: NavHostController,
    startDestination: String
) {
    HandleDeepLink(navController)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.MainScreen.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "spender://home"
            })
        ) {
            MainScreen(navController)
        }
        composable(Screen.OnboardingScreen.route) {
            OnboardingScreen(navController)
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(navController)
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
        composable(Screen.MyinfoScreen.route) {
            MyinfoScreen(navController)
        }
        composable(Screen.NotificationListScreen.route) {
            NotificationListScreen(navHostController = navController, homeViewModel = hiltViewModel())
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
                IncomeDetailScreen(navController, incomeId = incomeId)
            }
        }
        composable(Screen.RegularExpenseDetailScreen.route) { backStackEntry ->
            val regularExpenseId = backStackEntry.arguments?.getString("regularExpenseId")
            if (regularExpenseId != null) {
                RecurringExpenseDetailScreen(navController, regularExpenseId = regularExpenseId)
            }
        }
        composable(
            Screen.ExpenseRegistrationScreen.route,
            listOf(
                navArgument("selectedTabIndex") {
                    type = NavType.IntType
                    defaultValue = 1
                },
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "spender://expense_registration"
                }
            )
        ) { backStackEntry ->
            val selectedTabIndex = backStackEntry.arguments?.getInt("selectedTabIndex")
            ExpenseRegistrationParentScreen(
                navController,
                selectedTabIndex ?: 1
            )
        }
        composable(Screen.OcrResultScreen.route) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            OcrResultScreen(navController, title, amount, date)
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
        composable(Screen.OpenSourceScreen.route){
            OpenSourceScreen(navController)
        }
//        composable(Screen.ExpenseRegistrationScreen.route) {
//            ExpenseRegistrationParentScreen(navController)
//        }
        composable(Screen.IncomeRegistrationScreen.route) {
            IncomeRegistrationScreen(navController)
        }
        composable(Screen.FriendAddScreen.route) {
            FriendAddScreen(navController)
        }
    }
}

@Composable
private fun HandleDeepLink(navController: NavHostController) {
    val activity = androidx.activity.compose.LocalActivity.current as? MainActivity

    // 앱이 꺼진 상태에서 시작(콜드 스타트)
    LaunchedEffect(Unit) {
        activity?.intent?.let { navController.handleDeepLink(it) }
    }

    // 앱이 켜져 있는 상태에서 새 Intent 도착(onNewIntent)
    DisposableEffect(Unit) {
        val cb: (Intent) -> Unit = { intent -> navController.handleDeepLink(intent) }
        activity?.onNewIntentCallback = cb
        onDispose { activity?.onNewIntentCallback = null }
    }
}
