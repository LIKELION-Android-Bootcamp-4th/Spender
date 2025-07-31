package com.example.spender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spender.core.ui.BottomNavigationBar
import com.example.spender.feature.analysis.AnalysisScreen
import com.example.spender.feature.expense.ExpenseRegistrationScreen
import com.example.spender.feature.home.HomeScreen
import com.example.spender.feature.mypage.MypageScreen
import com.example.spender.feature.report.ReportScreen
import com.example.spender.ui.theme.SpenderTheme
import com.example.spender.ui.theme.navigation.BottomNavigationItem
import com.example.spender.ui.theme.navigation.SpenderNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val navController = rememberNavController()
//            SpenderNavigation(navController)
            SpenderTheme {
                ExpenseRegistrationScreen()
            }
        }
    }
}

@Composable
fun MainScreen(rootNavHostController: NavHostController) {
    val bottomBarNavController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(bottomBarNavController) }
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
                ReportScreen(rootNavHostController)
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