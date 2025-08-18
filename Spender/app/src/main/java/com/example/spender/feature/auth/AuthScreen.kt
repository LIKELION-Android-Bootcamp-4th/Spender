package com.example.spender.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.R
import com.example.spender.core.common.readRawTextFile
import com.example.spender.core.ui.TermsDialog
import com.example.spender.feature.auth.ui.GoogleLogin
import com.example.spender.feature.auth.ui.KakaoLogin
import com.example.spender.feature.auth.ui.NaverLogin
import com.example.spender.feature.auth.ui.viewmodel.AuthViewModel
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.WhiteColor

@Composable
fun AuthScreen(navController: NavHostController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var showTerms by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }

    val context = LocalContext.current

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
                Spacer(Modifier.height(72.dp))
                GoogleLogin(navController)
                Spacer(Modifier.height(24.dp))
                NaverLogin(navController)
                Spacer(Modifier.height(24.dp))
                KakaoLogin(navController)

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "로그인 시",
                        style = Typography.labelSmall,
                        color = LightFontColor
                    )

                    TextButton(
                        modifier = Modifier,
                        onClick = { showTerms = true }
                    ) {
                        Text(
                            text = "[이용약관]",
                            style = Typography.labelSmall.copy(
                                textDecoration = TextDecoration.Underline
                            ),
                            color = LightFontColor
                        )
                    }

                    Text(
                        text = "및",
                        style = Typography.labelSmall,
                        color = LightFontColor
                    )

                    TextButton(
                        modifier = Modifier,
                        onClick = { showPrivacy = true }
                    ) {
                        Text(
                            text = "[개인정보 처리방침]",
                            style = Typography.labelSmall.copy(
                                textDecoration = TextDecoration.Underline
                            ),
                            color = LightFontColor
                        )
                    }

                    Text(
                        text = "에 동의함으로 간주합니다.",
                        style = Typography.labelSmall,
                        color = LightFontColor
                    )

                }
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

        if (showTerms) {
            TermsDialog(
                title = "이용약관",
                content = readRawTextFile(context, R.raw.terms),
                onDismiss = { showTerms = false }
            )
        }

        if (showPrivacy) {
            TermsDialog(
                title = "개인정보 처리방침",
                content = readRawTextFile(context, R.raw.privacy),
                onDismiss = { showPrivacy = false }
            )
        }

    }
}