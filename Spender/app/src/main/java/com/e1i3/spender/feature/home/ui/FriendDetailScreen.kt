package com.e1i3.spender.feature.home.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar
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

//    LaunchedEffect {
//
//    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "친구 지출 현황", // TODO: 친구 이름으로 변경?
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
                    .padding(vertical = 10.dp, horizontal = 0.dp)
            ) {
                // TODO: 티어

                item {
                    BudgeProgress(
                        percentage = 20f, // TODO: 바꾸기
                        navHostController = navHostController,
                        showSetBudgetButton = false,
                        showNickname = true
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }


                item {
                    Text("xxx 님은", modifier = Modifier.padding(horizontal = 20.dp), style = Typography.titleMedium)
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = PointColor)) { // TODO : 바꾸기
                                append("식비") // TODO : 바꾸기
                            }
                            append("카테고리에서 많은 소비를 하고 있어요")
                        },
                        style = Typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.height(15.dp))
                    CategoryPieChart(
                        labels = listOf("식비", "교통", "쇼핑"),
                        values = listOf(35000f, 15000f, 20000f),
                        colors = listOf(
                            0xFF3182F6.toInt(), // 파란색
                            0xFFFEBB45.toInt(), // 노란색
                            0xFFE26C6C.toInt()  // 빨간색
                        )
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }

                item {
                    Text("xxx 님은", modifier = Modifier.padding(horizontal = 20.dp), style = Typography.titleMedium)
                    Text(
                        buildAnnotatedString {
                            append("이번달 ")
                            withStyle(style = SpanStyle(color = PointColor)) { // TODO : 바꾸기
                                append("억울") // TODO : 바꾸기
                            }
                            append("감정을 가장 많이 느끼고 있어요")
                        },
                        style = Typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.height(15.dp))
                    EmotionBarChart(
                        labels = listOf("만족", "불만", "충동", "억울"),
                        values = listOf(5f, 10f, 8f, 4f),
                        colors = listOf(
                            0xFF3182F6.toInt(),
                            0xFFFEBB45.toInt(),
                            0xFFE26C6C.toInt(),
                            0xFFE26C6C.toInt(),
                        )
                    )
                    Spacer(Modifier.height(15.dp))
                }
            }
        }
    )
}