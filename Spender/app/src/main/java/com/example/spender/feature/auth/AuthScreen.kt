package com.example.spender.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.R
import com.example.spender.feature.auth.ui.GoogleLogin
import com.example.spender.feature.auth.ui.KakaoLogin
import com.example.spender.feature.auth.ui.NaverLogin
import com.example.spender.feature.auth.ui.viewmodel.AuthViewModel
import com.example.spender.ui.theme.WhiteColor

@Composable
fun AuthScreen(navController: NavHostController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteColor)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(96.dp))
                Icon(
                    painter = painterResource(id = R.drawable.auth_image),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(screenWidth * 0.8f),
                )
                Spacer(Modifier.height(88.dp))
                GoogleLogin(navController)
                Spacer(Modifier.height(24.dp))
                NaverLogin(navController)
                Spacer(Modifier.height(24.dp))
                KakaoLogin(navController)
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}