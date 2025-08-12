package com.example.spender

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpOffset
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
import com.example.spender.feature.income.ui.IncomeRegistrationScreen
import com.example.spender.feature.mypage.MypageScreen
import com.example.spender.feature.onboarding.data.OnboardingPref
import com.example.spender.feature.report.ui.list.ReportListScreen
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.SpenderTheme
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.WhiteColor
import com.example.spender.ui.theme.navigation.BottomNavigationItem
import com.example.spender.ui.theme.navigation.Screen
import com.example.spender.ui.theme.navigation.SpenderNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var onNewIntentCallback: ((Intent) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpenderTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                val navController = rememberNavController()

                SpenderNavigation(
                    navController = navController,
                    startDestination = Screen.SplashScreen.route
                )

            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        onNewIntentCallback?.invoke(intent ?: return)
        setIntent(intent)
    }
}

@Composable
fun MainScreen(rootNavHostController: NavHostController) {
    val bottomBarNavController = rememberNavController()
    var isFabMenuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                FloatingActionButton(
                    onClick = { isFabMenuExpanded = true },
                    shape = RoundedCornerShape(72.dp),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    containerColor = WhiteColor,
                    contentColor = Color.Unspecified,
                    modifier = Modifier
                        .offset(y = 50.dp)
                        .size(72.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_floating_add),
                        contentDescription = "Add Expense",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                DropdownMenu(
                    expanded = isFabMenuExpanded,
                    onDismissRequest = { isFabMenuExpanded = false },
                    offset = DpOffset(x = (-20).dp, y = (45).dp),
                    modifier = Modifier
                        .background(PointColor)
                ) {
                    DropdownMenuItem(
                        text = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("수입 등록", color = Color.White)
                            }
                        },
                        onClick = {
                            rootNavHostController.navigate(Screen.IncomeRegistrationScreen.route)
                            isFabMenuExpanded = false
                        }
                    )
                    HorizontalDivider(color = Color.White)
                    DropdownMenuItem(
                        text = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("지출 등록", color = Color.White)
                            }
                        },
                        onClick = {
                            rootNavHostController.navigate("expense_registration/1")
                            isFabMenuExpanded = false
                        }
                    )
                }
            }
        },
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

@Composable
private fun HandlePushNavigation(
    rootNavController: NavHostController,
    bottomNavController: NavHostController
) {
    val activity = LocalActivity.current as MainActivity

    fun navigateByExtras(intent: Intent) {
        val route = intent.getStringExtra("route") ?: return
        val month = intent.getStringExtra("month")
        val regularExpenseName = intent.getStringExtra("regularExpenseName")

        // 탭 이동
        when (route) {
            "home" -> bottomNavController.navigate(BottomNavigationItem.Home.route)
            "stats" -> bottomNavController.navigate(BottomNavigationItem.Analysis.route)
            "reports" -> bottomNavController.navigate(BottomNavigationItem.Report.route)
        }

        // 선택: 리포트 상세 바로 열기 (month가 왔을 때)
        if (route == "reports" && month != null) {
            rootNavController.navigate(
                com.example.spender.ui.theme.navigation.Screen.ReportDetail.createRoute(month)
            )
        }

        // 중복 처리 방지를 위해 extras 제거
        intent.replaceExtras(android.os.Bundle())
    }

    // 앱이 꺼진 상태에서 시작(콜드스타트)
    LaunchedEffect(Unit) {
        navigateByExtras(activity.intent)
    }

    // 앱이 켜진 상태에서 알림 클릭(onNewIntent)
    DisposableEffect(Unit) {
        val cb: (Intent) -> Unit = { intent -> navigateByExtras(intent) }
        activity.onNewIntentCallback = cb
        onDispose { activity.onNewIntentCallback = null }
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