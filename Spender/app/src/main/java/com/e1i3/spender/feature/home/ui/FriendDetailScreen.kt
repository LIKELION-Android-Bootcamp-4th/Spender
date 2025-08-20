package com.e1i3.spender.feature.home.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.core.ui.LoadingScreen
import com.e1i3.spender.feature.home.ui.model.FriendDetailUiModel
import com.e1i3.spender.feature.home.ui.viewModel.FriendDetailViewModel
import com.e1i3.spender.feature.report.ui.component.CategoryPieChart
import com.e1i3.spender.feature.report.ui.component.EmotionBarChart
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun FriendDetailScreen(
    navHostController: NavHostController,
    friendId: String,
    viewModel: FriendDetailViewModel = hiltViewModel()
) {
    Log.d("FriendDetailScreen", "Composable started with friendId = $friendId")

    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val friend = viewModel.friendDetail.value

    Log.d("FriendDetailScreen", "friend = $friend, isLoading = $isLoading, error = $error")

    LaunchedEffect(friendId) {
        Log.d("FriendDetail", "loadFriendDetail called with $friendId")
        viewModel.loadFriendDetail(friendId)
    }

    when {
        isLoading -> LoadingScreen()
        friend == null -> {
            Text("친구 정보를 찾을 수 없습니다.")
        }
        else -> {
            Scaffold(
                topBar = {
                    CustomTopAppBar(
                        title = "${friend.nickname} 지출 현황",
                        showBackButton = true,
                        navController = navHostController,
                        actions = {
                            // TODO : 친구 삭제
                        }
                    )
                },
                content = { padding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(padding)
                            .padding(vertical = 10.dp, horizontal = 8.dp)
                    ) {
                        // TODO: 티어

                        item {
                            BudgetProgressSection(friend = friend, navHostController = navHostController
                            )
                        }

                        item {
                            CategorySection(friend = friend)
                        }

                        item {
                            EmotionSection(friend = friend)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun BudgetProgressSection(
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
fun CategorySection(
    friend: FriendDetailUiModel,
){
    Text(
        "${friend.nickname}님은",
        modifier = Modifier.padding(horizontal = 20.dp),
        style = Typography.titleMedium
    )
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = friend.topCategoryColor)) { // TODO : 바꾸기
                append("${friend.topCategoryName}")
            }
            append("카테고리에서 많은 소비를 하고 있어요")
        },
        style = Typography.titleMedium,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
    Spacer(Modifier.height(15.dp))
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
fun EmotionSection(
    friend: FriendDetailUiModel
){
    Text(
        "${friend.nickname}님은",
        modifier = Modifier.padding(horizontal = 20.dp),
        style = Typography.titleMedium
    )
    Text(
        buildAnnotatedString {
            append("이번달 ")
            withStyle(style = SpanStyle(color = friend.topEmotionColor)) { // TODO : 바꾸기
                append("${friend.topEmotionName}")
            }
            append("감정을 가장 많이 느끼고 있어요")
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
