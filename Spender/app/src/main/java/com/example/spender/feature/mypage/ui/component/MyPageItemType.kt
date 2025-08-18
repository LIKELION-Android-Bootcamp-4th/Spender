package com.example.spender.feature.mypage.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.IndeterminateCheckBox
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MyPageItemType(val title: String, val icon: ImageVector) {
    object IncomeCategory : MyPageItemType("수입 카테고리 설정", Icons.Outlined.AddBox)
    object ExpenseCategory : MyPageItemType("지출 카테고리 설정", Icons.Outlined.IndeterminateCheckBox)
    object Budget : MyPageItemType("예산 설정", Icons.Outlined.Create)
    object RegularExpense : MyPageItemType("정기지출 모아보기", Icons.AutoMirrored.Outlined.List)
    object Notification : MyPageItemType("알림 설정", Icons.Outlined.Notifications)
    object Withdraw : MyPageItemType("회원 탈퇴", Icons.Outlined.PersonRemove)
    object Logout : MyPageItemType("로그아웃", Icons.AutoMirrored.Outlined.Logout)
}
