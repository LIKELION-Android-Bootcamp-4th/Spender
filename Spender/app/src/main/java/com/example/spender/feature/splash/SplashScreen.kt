package com.example.spender.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.R
import com.example.spender.feature.splash.ui.SplashViewModel
import com.example.spender.feature.splash.ui.SplashViewModel.SplashUiState
import com.example.spender.ui.theme.navigation.Screen
import androidx.compose.runtime.getValue

@Composable
fun SplashScreen(
    navController: NavHostController,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by splashViewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            SplashUiState.NavigateToAuth -> {
                navController.navigate(Screen.AuthScreen.route) {
                    popUpTo(Screen.SplashScreen.route) { inclusive = true }
                }
            }
            SplashUiState.NavigateToMain -> {
                navController.navigate(Screen.MainScreen.route) {
                    popUpTo(Screen.SplashScreen.route) { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ci_temp),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(250.dp),
        )
    }
}