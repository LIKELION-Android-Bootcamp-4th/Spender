package com.e1i3.spender.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.e1i3.spender.feature.home.ui.model.FriendDetailUiModel
import com.e1i3.spender.feature.report.ui.component.CategoryPieChart
import com.e1i3.spender.feature.report.ui.component.EmotionBarChart
import com.e1i3.spender.feature.report.ui.model.CategoryUiModel
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
    if(friend.nickname == "") friend.nickname = "알 수 없음"

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
            append(" 카테고리 지출이 많아요")
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
    Spacer(modifier = Modifier.height(5.dp))
    FriendCategoryList(friend.categoryChartData)
    Spacer(modifier = Modifier.height(30.dp))
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
    Spacer(Modifier.height(30.dp))
}

@Composable
fun FriendCategoryList(categories : List<CategoryUiModel>) {

    val sortedCategories = categories.sortedByDescending { it.percentage }

    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        sortedCategories.forEach { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(category.color, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = category.label, style = Typography.bodyMedium, fontWeight = FontWeight.Medium)
                }
                Text(text = "${category.percentage.toInt()}%", style = Typography.bodyMedium)
            }
        }
    }
}
