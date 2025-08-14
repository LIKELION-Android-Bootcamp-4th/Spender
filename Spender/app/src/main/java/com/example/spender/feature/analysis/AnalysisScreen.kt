package com.example.spender.feature.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.spender.feature.analysis.ui.calendar.CalendarScreen
import com.example.spender.feature.analysis.ui.graph.GraphScreen
import com.example.spender.ui.theme.SpenderTheme
import com.example.spender.ui.theme.Typography

@Composable
fun AnalysisScreen(navHostController: NavHostController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("캘린더", "그래프")

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(20.dp))

        // 탭 메뉴
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            containerColor = MaterialTheme.colorScheme.tertiary,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .fillMaxSize()
                        .padding(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                Box(
                    modifier = Modifier
                        .height(52.dp)
                        .fillMaxHeight()
                        .zIndex(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { selectedTabIndex = index }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = Typography.titleMedium,
                        color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TabContent(selectedTabIndex, navHostController)
    }
}


@Composable
fun TabContent(selectedTabIndex: Int, navHostController: NavHostController) {
    when (selectedTabIndex) {
        0 -> CalendarScreen(navHostController = navHostController)
        1 -> GraphScreen(navHostController = navHostController)
    }
}

@Preview(showBackground = true)
@Composable
fun AnalysisPreview() {
    SpenderTheme {
        val tabs = listOf("캘린더", "그래프")

        TabRow(
            selectedTabIndex = 0,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            containerColor = MaterialTheme.colorScheme.tertiary,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[0])
                        .fillMaxSize()
                        .padding(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = 0 == index
                Box(
                    modifier = Modifier
                        .height(52.dp)
                        .fillMaxHeight()
                        .zIndex(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = Typography.titleMedium,
                        color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}