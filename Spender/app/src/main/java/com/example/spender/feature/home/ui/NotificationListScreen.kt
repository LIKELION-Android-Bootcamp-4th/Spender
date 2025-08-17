package com.example.spender.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.feature.home.ui.component.NotificationItem
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.Typography

@Composable
fun NotificationListScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    viewModel: NotificationListViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading
    val items by viewModel.notifications
    val error by viewModel.error

    LaunchedEffect(Unit) {
        viewModel.load()
        viewModel.markAllAsRead()
        homeViewModel.checkUnreadNotifications()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "알림",
                showBackButton = true,
                navController = navHostController,
                actions = {
                    TextButton(onClick = { navHostController.navigate("notification") }) {
                        Text(text = "설정", style = Typography.bodyMedium)
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> Text("불러오는 중...", Modifier.padding(padding).padding(24.dp))
            error != null -> Text("오류: $error", Modifier.padding(padding).padding(24.dp))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items, key = { it.id }) { n ->
                        // NotificationItem이 날짜 문자열을 받는다면 아래 주석을 해제해서 전달하세요.
                        // val dateText = remember(n.date) {
                        //     SimpleDateFormat("M월 d일", Locale.KOREA).format(n.date)
                        // }
                        NotificationItem(
                            notification = n,
                            rootNavController = navHostController
                            // , dateText = dateText
                            // , onClick = { viewModel.markAsRead(n.id) }  // 읽음 처리 쓰면 추가
                        )
                    }
                    item {
                        Text(
                            text = "7일 전 알림까지 확인할 수 있어요.",
                            style = Typography.labelMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            textAlign = TextAlign.Center,
                            color = LightFontColor
                        )
                    }
                }
            }
        }
    }
}
