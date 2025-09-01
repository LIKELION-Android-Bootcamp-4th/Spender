package com.e1i3.spender

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.e1i3.spender.core.ui.BottomNavigationBar
import com.e1i3.spender.feature.analysis.AnalysisScreen
import com.e1i3.spender.feature.home.HomeScreen
import com.e1i3.spender.feature.mypage.MypageScreen
import com.e1i3.spender.feature.mypage.data.repository.FriendRepository
import com.e1i3.spender.feature.report.ui.list.ReportListScreen
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.SpenderTheme
import com.e1i3.spender.ui.theme.Typography
import com.e1i3.spender.ui.theme.WhiteColor
import com.e1i3.spender.ui.theme.navigation.BottomNavigationItem
import com.e1i3.spender.ui.theme.navigation.Screen
import com.e1i3.spender.ui.theme.navigation.SpenderNavigation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var onNewIntentCallback: ((Intent) -> Unit)? = null
    var pendingDeepLink: Intent? = null
    val repository = FriendRepository(FirebaseAuth.getInstance())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (intent.data != null) {
            val code = intent.data.toString()
            if(code.contains("invite")){
                handleDeepLink(code.substring(code.length-8, code.length))
            }
        }

        setContent {
            SpenderTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()

                RootIntentHandler(navController)

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

        val data = intent.data
        if (data != null && data.toString().contains("invite")) {
            handleDeepLink(data.toString().takeLast(8))
            return
        }
        onNewIntentCallback?.invoke(intent)
    }


    private fun handleDeepLink(code: String) {
        lifecycleScope.launch {
            val msg = repository.addFriend(code)
            showMessage(msg)
        }
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun RootIntentHandler(rootNavController: NavHostController) {
    val activity = LocalActivity.current as MainActivity

    fun handleUriDeepLink(intent: Intent): Boolean {
        val data = intent.data ?: return false
        if (data.scheme != "spender") return false

        when (data.host) {
            "home" -> {
                rootNavController.navigate(Screen.MainScreen.route) {
                    launchSingleTop = true
                }
            }
            "expense_registration" -> {
                rootNavController.navigate("expense_registration/0") {
                    launchSingleTop = true
                }
            }
            "income_registration" -> {
                rootNavController.navigate(Screen.IncomeRegistrationScreen.route) {
                    launchSingleTop = true
                }
            }
            "report_detail" -> {
                val month = data.pathSegments.getOrNull(0) ?: return false
                rootNavController.navigate(Screen.ReportDetail.createRoute(month)) {
                    launchSingleTop = true
                }
            }
            else -> return false
        }

        intent.data = null
        intent.replaceExtras(Bundle())
        return true
    }

    fun handleRouteExtra(intent: Intent): Boolean {
        val route = intent.getStringExtra("route") ?: return false
        when {
            route == "home" -> {
                rootNavController.navigate(Screen.MainScreen.route) { launchSingleTop = true }
            }
            route == "analysis" -> {
                rootNavController.navigate(BottomNavigationItem.Analysis.route) {
                    launchSingleTop = true
                }
            }
            route.startsWith("report_detail/") -> {
                val month = route.removePrefix("report_detail/")
                rootNavController.navigate(Screen.ReportDetail.createRoute(month)) {
                    launchSingleTop = true
                }
            }
            route == "add_expense" -> {
                rootNavController.navigate("expense_registration/0") { launchSingleTop = true }
            }
            route == "add_income" -> {
                rootNavController.navigate(Screen.IncomeRegistrationScreen.route) { launchSingleTop = true }
            }
            else -> {
                rootNavController.navigate(Screen.MainScreen.route) { launchSingleTop = true }
            }
        }
        intent.replaceExtras(Bundle())
        return true
    }

    fun tryNavigate(intent: Intent): Boolean {
        if (handleUriDeepLink(intent)) return true
        if (handleRouteExtra(intent)) return true
        return false
    }

    LaunchedEffect(rootNavController) {
        rootNavController.currentBackStackEntryFlow.first()
        activity.intent?.let { intent ->
            if (!tryNavigate(intent)) {
                activity.pendingDeepLink = intent
            }
        }
    }

    // 그래프 변화 시마다 pending 재시도
    LaunchedEffect(rootNavController.currentBackStackEntry) {
        activity.pendingDeepLink?.let { pending ->
            if (tryNavigate(pending)) {
                activity.pendingDeepLink = null
            }
        }
    }

    // 포그라운드 새 인텐트 도착 시
    DisposableEffect(Unit) {
        val cb: (Intent) -> Unit = { intent ->
            if (!tryNavigate(intent)) {
                activity.pendingDeepLink = intent
            }
        }
        activity.onNewIntentCallback = cb
        onDispose { activity.onNewIntentCallback = null }
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