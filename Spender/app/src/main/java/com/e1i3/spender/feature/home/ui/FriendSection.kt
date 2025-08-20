package com.e1i3.spender.feature.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.e1i3.spender.feature.home.ui.model.FriendDetailUiModel
import com.e1i3.spender.feature.report.ui.component.CategoryPieChart
import com.e1i3.spender.feature.report.ui.component.EmotionBarChart
import com.e1i3.spender.ui.theme.Typography

@Composable
fun FriendBudgetProgressSection(
    friend: FriendDetailUiModel,
    navHostController: NavHostController
){
    BudgeProgress(
        percentage = friend.budgetProgress,
        nickname = friend.nickname,
        navHostController = navHostController,
        showSetBudgetButton = false,
        showNickname = true
    )
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun FriendCategorySection(
    friend: FriendDetailUiModel,
){
    Text(
        "${friend.nickname}님은",
        modifier = Modifier.padding(horizontal = 20.dp),
        style = Typography.titleMedium
    )
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = friend.topCategoryColor)) {
                append("${friend.topCategoryName}")
            }
            append("카테고리 지출이 많아요")
        },
        style = Typography.titleMedium,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
    Spacer(Modifier.height(20.dp))
    Box(
        modifier = Modifier.padding(horizontal = 20.dp)
    ){
        CategoryPieChart(
            labels = friend.categoryChartData.map { it.label },
            values = friend.categoryChartData.map { it.percentage },
            colors = friend.categoryChartData.map { it.color.toArgb() })
    }
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun FriendEmotionSection(
    friend: FriendDetailUiModel
){
    Text(
        "${friend.nickname}님은",
        modifier = Modifier.padding(horizontal = 20.dp),
        style = Typography.titleMedium
    )
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = friend.topEmotionColor)) {
                append("${friend.topEmotionName}")
            }
            append(" 감정을 가장 많이 느끼고 있어요")
        },
        style = Typography.titleMedium,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
    Spacer(Modifier.height(10.dp))
    Box(
        modifier = Modifier.padding(horizontal = 20.dp)
    ){
        EmotionBarChart(
            labels = friend.emotionChartData.map { it.label },
            values = friend.emotionChartData.map { it.amount.toFloat() },
            colors = friend.emotionChartData.map { it.color.toArgb() }
        )
    }
    Spacer(Modifier.height(15.dp))
}
