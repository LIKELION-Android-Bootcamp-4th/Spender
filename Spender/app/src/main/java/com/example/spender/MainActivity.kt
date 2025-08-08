package com.example.spender

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spender.core.ui.BottomNavigationBar
import com.example.spender.feature.analysis.AnalysisScreen
import com.example.spender.feature.expense.ui.ExpenseRegistrationParentScreen
import com.example.spender.feature.home.HomeScreen
import com.example.spender.feature.mypage.MypageScreen
import com.example.spender.feature.onboarding.data.OnboardingPref
import com.example.spender.feature.report.ui.list.ReportListScreen
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.SpenderTheme
import com.example.spender.ui.theme.navigation.BottomNavigationItem
import com.example.spender.ui.theme.navigation.Screen
import com.example.spender.ui.theme.navigation.SpenderNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                // 로그인 화면으로 가려면 아래 주석 해제하고
                val startDestination = Screen.AuthScreen.route
                //이거 아래부분 주석처리 하면 됨
//                val startDestination = if (isOnboardingShown) {
//                    Screen.MainScreen.route
//                } else {
//                    Screen.OnboardingScreen.route
//                }
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
        floatingActionButton = { FloatingActionButton(
            onClick = {rootNavHostController.navigate(Screen.ExpenseRegistrationScreen.route)},
            shape = RoundedCornerShape(72.dp),
            containerColor = PointColor,
            modifier = Modifier.offset(y = 50.dp).size(72.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Expense",
                tint = Color.White
            )
        } },
        floatingActionButtonPosition = FabPosition.Center,
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