package com.example.spender.feature.auth.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.R
import com.example.spender.feature.auth.ui.viewmodel.AuthViewModel
import com.example.spender.ui.theme.BlackColor
import com.example.spender.ui.theme.KakaoColor
import com.example.spender.ui.theme.KakaoLabelColor
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.WhiteColor

@Composable
fun KakaoLogin(
    navController: NavHostController
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, color = KakaoColor),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhiteColor
        ),
        onClick = {
            viewModel.kakaoLogin(context, navController)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.kakao_icon),
                contentDescription = null,
                tint = KakaoLabelColor,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "카카오 계정으로 시작하기",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typography.bodyMedium.copy(
                    color = BlackColor
                )
            )
        }
    }
}