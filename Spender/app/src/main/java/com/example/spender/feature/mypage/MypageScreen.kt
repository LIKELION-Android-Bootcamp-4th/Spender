package com.example.spender.feature.mypage

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.feature.mypage.ui.component.MyPageItemType
import com.example.spender.feature.mypage.ui.component.Section

@Composable
fun MypageScreen(navHostController: NavHostController) {
    val userName = "이수지"

    val onItemClick: (MyPageItemType) -> Unit = { item ->
        when(item){
            MyPageItemType.IncomeCategory -> navHostController.navigate("income_category")
            MyPageItemType.ExpenseCategory -> navHostController.navigate("expense_category")
            MyPageItemType.Budget -> navHostController.navigate("budget")
            MyPageItemType.RegularExpense -> navHostController.navigate("regular_expense")
            MyPageItemType.Notification -> navHostController.navigate("notification")
            MyPageItemType.Withdraw -> {
                // TODO: 탈퇴 다이얼로그
            }
            MyPageItemType.Logout -> {
                // TODO: 로그아웃 다이얼로그
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 14.dp)
    ) {
        UserInfoSection(userName = userName)

        //HorizontalDivider()

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

        //HorizontalDivider()


        Section(
            title = "설정",
            items = listOf(
                MyPageItemType.Notification,
                MyPageItemType.Withdraw,
                MyPageItemType.Logout
            ),
            onItemClick = onItemClick
        )

        AdBanner()
    }
}

@Composable
fun UserInfoSection(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp, horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(24.dp)) // TODO : 소셜 로그인에 맞는 이미지로 교체
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

