package com.e1i3.spender.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.e1i3.spender.core.common.util.getRelativeTimeString
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.core.ui.LoadingScreen
import com.e1i3.spender.feature.home.ui.component.NotificationItem
import com.e1i3.spender.ui.theme.LightFontColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun NotificationListScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    viewModel: NotificationListViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading
    val items by viewModel.notifications
    val error by viewModel.error

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentViewModel by rememberUpdatedState(viewModel)
    val currentHomeViewModel by rememberUpdatedState(homeViewModel)

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                currentViewModel.markAllAsRead()
                currentHomeViewModel.checkUnreadNotifications()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
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
            isLoading -> LoadingScreen()
            error != null -> Text("오류: $error", Modifier.padding(padding).padding(24.dp))
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 0.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(items, key = { it.id }) { n ->
                        NotificationItem(
                            notification = n,
                            rootNavController = navHostController,
                            dateText = getRelativeTimeString(n.date)
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
