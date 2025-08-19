package com.e1i3.spender.feature.mypage.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.e1i3.spender.core.data.local.LicenseLocalDataSource
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.feature.mypage.ui.component.OpenSourceItem

@Composable
fun OpenSourceScreen(navHostController: NavHostController){
    val context = LocalContext.current
    val licenses = remember { LicenseLocalDataSource().loadLicenses(context) }


    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "오픈소스 라이선스",
                navHostController,
                showBackButton = true,
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(licenses) { license ->
                    OpenSourceItem(license)
                }
            }

        }
    )
}
