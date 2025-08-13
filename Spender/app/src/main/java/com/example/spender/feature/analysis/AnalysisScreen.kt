package com.example.spender.feature.analysis

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.feature.analysis.ui.calendar.CalendarScreen
import com.example.spender.feature.analysis.ui.graph.GraphScreen
import com.example.spender.ui.theme.SpenderTheme

@Composable
fun AnalysisScreen(navHostController: NavHostController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("캘린더", "그래프")

    Column(modifier = Modifier.fillMaxSize()) {
        TabSelector(
            tabs = tabTitles,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it })

        Spacer(modifier = Modifier.height(30.dp))

        TabContent(selectedTabIndex, navHostController)
    }
}

@Composable
fun TabSelector(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            val transition =
                updateTransition(targetState = selectedTabIndex, label = "tab_transition")
            val indicatorOffset by transition.animateDp(
                transitionSpec = { tween(250) },
                label = "indicator_offset"
            ) {
                val fullWidth = LocalConfiguration.current.screenWidthDp.dp - 24.dp * 2
                (fullWidth / 2) * it
            }

            val indicatorWidth = with(LocalDensity.current) {
                ((LocalConfiguration.current.screenWidthDp.dp - 24.dp * 2 - 4.dp) / 2)
            }

            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(indicatorWidth)
                    .height(43.dp)
                    .padding(2.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp)
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource
                            ) { onTabSelected(index) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
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
        TabSelector(listOf("캘린더", "그래프"), 0, { })
    }
}