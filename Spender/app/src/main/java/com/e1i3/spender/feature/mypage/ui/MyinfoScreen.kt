package com.e1i3.spender.feature.mypage.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.feature.mypage.ui.viewmodel.MypageViewModel

@Composable
fun MyinfoScreen(
    navHostController: NavHostController,
    mypageViewModel: MypageViewModel = hiltViewModel(),
) {
    val user by mypageViewModel.user.collectAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "내 정보",
                navHostController,
                showBackButton = true
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 8.dp),
            ) {
                ProfileSection(nickName = user.displayNickname)
                Spacer(Modifier.height(64.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.tertiary,
                    thickness = 1.dp,
                )
                Spacer(Modifier.height(32.dp))
                AccountInfoSection(userName = user.displayName, userEmail = user.displayEmail, iconRes = user.providerIcon)
            }
        }
    )
}
