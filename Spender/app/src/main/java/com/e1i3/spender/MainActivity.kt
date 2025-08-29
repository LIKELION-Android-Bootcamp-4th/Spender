package com.e1i3.spender

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.e1i3.spender.core.ui.BottomNavigationBar
import com.e1i3.spender.feature.analysis.AnalysisScreen
import com.e1i3.spender.feature.home.HomeScreen
import com.e1i3.spender.feature.mypage.MypageScreen
import com.e1i3.spender.feature.report.ui.list.ReportListScreen
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.SpenderTheme
import com.e1i3.spender.ui.theme.Typography
import com.e1i3.spender.ui.theme.WhiteColor
import com.e1i3.spender.ui.theme.navigation.BottomNavigationItem
import com.e1i3.spender.ui.theme.navigation.Screen
import com.e1i3.spender.ui.theme.navigation.SpenderNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var onNewIntentCallback: ((Intent) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpenderTheme(
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
        setIntent(intent)
        onNewIntentCallback?.invoke(intent ?: return)
    }
}

@Composable
fun MainScreen(rootNavHostController: NavHostController) {
    val bottomBarNavController = rememberNavController()
    var isFabMenuExpanded by remember { mutableStateOf(false) }

    val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        isFabMenuExpanded = false
    }

    HandlePushNavigation(
        rootNavController = rootNavHostController,
        bottomNavController = bottomBarNavController
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isFabMenuExpanded = !isFabMenuExpanded },
                shape = RoundedCornerShape(72.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                containerColor = PointColor,
                contentColor = WhiteColor,
                modifier = Modifier
                    .offset(y = 50.dp)
                    .size(65.dp)
            ) {
                Icon(
                    imageVector = if (isFabMenuExpanded) Icons.Rounded.Close else Icons.Rounded.Add,
                    contentDescription = "Add Expense",
                    modifier = if (isFabMenuExpanded) Modifier.size(43.dp) else Modifier.size(50.dp),
                    tint = WhiteColor
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            Box(
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
        },
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                if (isFabMenuExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(0.5f)
                            .background(Color.Transparent)
                            .clickable {
                                isFabMenuExpanded = false
                            }
                    )
                }

                NavHost(
                    navController = bottomBarNavController,
                    startDestination = BottomNavigationItem.Home.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(BottomNavigationItem.Home.route) {
                        HomeScreen(rootNavHostController, bottomBarNavController)
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

                if (isFabMenuExpanded) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (-155).dp)
                            .zIndex(1f)
                    ) {
                        // 수입 등록
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .width(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PointColor)
                                .clickable {
                                    rootNavHostController.navigate(Screen.IncomeRegistrationScreen.route)
                                    isFabMenuExpanded = false
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = "수입 등록",
                                style = Typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                ),
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        // 지출 등록
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .width(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PointColor)
                                .clickable {
                                    rootNavHostController.navigate("expense_registration/0")
                                    isFabMenuExpanded = false
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = "지출 등록",
                                style = Typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                ),
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun HandlePushNavigation(
    rootNavController: NavHostController,
    bottomNavController: NavHostController
) {
    val activity = LocalActivity.current as MainActivity

    fun navigateByExtras(intent: Intent) {
        // URI 딥링크 먼저 처리 (위젯 등)
        intent.data?.let { data ->
            if (intent.action == Intent.ACTION_VIEW && data.scheme == "spender") {
                when (data.host) {
                    "expense_registration" -> {
                        bottomNavController.navigate(BottomNavigationItem.Home.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                        rootNavController.navigate("expense_registration/0") {
                            launchSingleTop = true
                        }
                    }
                    "income_registration" -> {
                        bottomNavController.navigate(BottomNavigationItem.Home.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                        rootNavController.navigate(Screen.IncomeRegistrationScreen.route) {
                            launchSingleTop = true
                        }
                    }
                    "home" -> {
                        bottomNavController.navigate(BottomNavigationItem.Home.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
                intent.data = null
                intent.replaceExtras(Bundle())
                return
            }
        }

        // FCM 등에서 extras로 들어오는 경우 처리
        val route = intent.getStringExtra("route") ?: run {
            // route가 없으면 기본 Home으로만 보냄 (URI가 있었다면 위에서 return 됨)
            bottomNavController.navigate(BottomNavigationItem.Home.route)
            intent.replaceExtras(Bundle())
            return
        }

        // 탭 이동
        when {
            route == "home" -> bottomNavController.navigate(BottomNavigationItem.Home.route)
            route == "analysis" -> bottomNavController.navigate(BottomNavigationItem.Analysis.route)
            route.startsWith("report_detail/") -> {
                val extractedMonth = route.removePrefix("report_detail/")
                bottomNavController.navigate(BottomNavigationItem.Report.route)
                rootNavController.navigate(
                    Screen.ReportDetail.createRoute(extractedMonth)
                )
            }
            route == "add_expense" -> {
                bottomNavController.navigate(BottomNavigationItem.Home.route) {
                    launchSingleTop = true
                    restoreState = true
                }
                rootNavController.navigate("expense_registration/0") {
                    launchSingleTop = true
                }
            }
            route == "add_income" -> {
                bottomNavController.navigate(BottomNavigationItem.Home.route) {
                    launchSingleTop = true
                    restoreState = true
                }
                rootNavController.navigate(Screen.IncomeRegistrationScreen.route) {
                    launchSingleTop = true
                }
            }
            else -> bottomNavController.navigate(BottomNavigationItem.Home.route)
        }
        intent.replaceExtras(Bundle())
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