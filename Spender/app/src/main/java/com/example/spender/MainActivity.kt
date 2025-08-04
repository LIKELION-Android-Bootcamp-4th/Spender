package com.example.spender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spender.core.ui.BottomNavigationBar
import com.example.spender.feature.analysis.AnalysisScreen
import com.example.spender.feature.expense.ui.ExpenseRegistrationParentScreen
import com.example.spender.feature.expense.ui.ocrresult.OcrResultScreen
import com.example.spender.feature.expense.ui.recurringexpensedetail.RecurringExpenseDetailScreen
import com.example.spender.feature.home.HomeScreen
import com.example.spender.feature.mypage.MypageScreen
import com.example.spender.feature.onboarding.data.OnboardingPref
import com.example.spender.feature.report.ui.list.ReportListScreen
import com.example.spender.ui.theme.SpenderTheme
import com.example.spender.ui.theme.navigation.BottomNavigationItem
import com.example.spender.ui.theme.navigation.Screen
import com.example.spender.ui.theme.navigation.SpenderNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpenderTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                val navController = rememberNavController()

                val isOnboardingShown = OnboardingPref.wasShown(this)
                val startDestination = if (isOnboardingShown) {
                    Screen.MainScreen.route
                } else {
                    Screen.OnboardingScreen.route
                }

                SpenderNavigation(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}

@Composable
fun MainScreen(rootNavHostController: NavHostController) {
    val bottomBarNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            Box( // 상단 모서리 둥글게 처리
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color.Transparent)
                    .border(
                        width = 0.3.dp,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            ) {
                BottomNavigationBar(bottomBarNavController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomBarNavController,
            startDestination = BottomNavigationItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavigationItem.Home.route) {
                HomeScreen(rootNavHostController)
            }
            composable(BottomNavigationItem.Analysis.route) {
                AnalysisScreen(rootNavHostController)
            }
            composable(BottomNavigationItem.Report.route) {
                ReportListScreen(rootNavHostController)
            }
            composable(BottomNavigationItem.Mypage.route) {
                MypageScreen(rootNavHostController)
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SpenderTheme {
//        Greeting("Android")
//    }
//}