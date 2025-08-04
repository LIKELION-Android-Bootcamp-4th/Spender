package com.example.spender.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.R
import com.example.spender.feature.auth.ui.GoogleLogin
import com.example.spender.feature.auth.ui.KakaoLogin
import com.example.spender.feature.auth.ui.NaverLogin
import com.example.spender.ui.theme.WhiteColor

@Composable
fun AuthScreen(
    navController: NavHostController
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
            Spacer(Modifier.height(88.dp))
            Icon(
                // TODO: 임시..? 아이콘
                painter = painterResource(id = R.drawable.temp_app_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(250.dp),
            )
            Spacer(Modifier.height(224.dp))
            GoogleLogin(navController)
            Spacer(Modifier.height(24.dp))
            NaverLogin()
            Spacer(Modifier.height(24.dp))
            KakaoLogin()
        }
    }
}