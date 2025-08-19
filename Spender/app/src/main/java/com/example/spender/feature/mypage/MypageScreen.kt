package com.example.spender.feature.mypage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomDialog
import com.example.spender.feature.auth.ui.viewmodel.AuthViewModel
import com.example.spender.feature.mypage.ui.component.MyPageItemType
import com.example.spender.feature.mypage.ui.component.Section
import com.example.spender.feature.mypage.ui.viewmodel.MypageViewModel
import com.example.spender.ui.theme.LightFontColor
import com.example.spender.ui.theme.Typography
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


@Composable
fun MypageScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    mypageViewModel: MypageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val user by mypageViewModel.user.collectAsState()

//    val viewModel: AuthViewModel = hiltViewModel()

    var showWithdrawDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
//    val userName = "이름"

    val onItemClick: (MyPageItemType) -> Unit = { item ->
        when (item) {
            MyPageItemType.IncomeCategory -> navHostController.navigate("income_category")
            MyPageItemType.ExpenseCategory -> navHostController.navigate("expense_category")
            MyPageItemType.Budget -> navHostController.navigate("budget")
            MyPageItemType.RegularExpense -> navHostController.navigate("regular_expense")
            MyPageItemType.Notification -> navHostController.navigate("notification")
            MyPageItemType.Withdraw -> {
                showWithdrawDialog = true
            }

            MyPageItemType.Logout -> {
                showLogoutDialog = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 14.dp)
    ) {
        UserInfoSection(
            userName = user.displayName,
            iconRes = user.providerIcon
        )

        Section(
            title = "가계부",
            items = listOf(
                MyPageItemType.IncomeCategory,
                MyPageItemType.ExpenseCategory,
                MyPageItemType.Budget,
                MyPageItemType.RegularExpense
            ),
            onItemClick = onItemClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        Section(
            title = "설정",
            items = listOf(
                MyPageItemType.Notification,
                MyPageItemType.Withdraw,
                MyPageItemType.Logout
            ),
            onItemClick = onItemClick
        )

        TextButton(
            onClick = {
                navHostController.navigate("open_source")
            }
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "오픈소스 라이선스 보기",
                style = Typography.bodyMedium.copy(textDecoration = TextDecoration.Underline, color = LightFontColor)
            )
        }

        AdMobBanner()
    }

    if (showWithdrawDialog) {
        CustomDialog(
            title = "탈퇴하시겠습니까?",
            onConfirm = {
                showWithdrawDialog = false

                authViewModel.withdraw(
                    context,
                    onSuccess = {
                        Toast.makeText(context, "탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT)
                            .show()
                        navHostController.navigate("auth") {
                            popUpTo("main") { inclusive = true }
                        }
                    },
                    onError = { msg ->
                        Toast.makeText(context, "회원탈퇴 실패 $msg", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onDismiss = {
                showWithdrawDialog = false
            }
        )
    }
    if (showLogoutDialog) {
        CustomDialog(
            title = "로그아웃 하시겠습니까?",
            onConfirm = {
                showLogoutDialog = false

                authViewModel.logout(context) {
                    Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                    navHostController.navigate("auth") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }

}

@Composable
fun AdMobBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}


@Composable
fun UserInfoSection(userName: String, iconRes: Int?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp, horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = "소셜 로그인 아이콘",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text("$userName 님", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun AdBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .height(50.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("광고 시간 나면")
    }
}

