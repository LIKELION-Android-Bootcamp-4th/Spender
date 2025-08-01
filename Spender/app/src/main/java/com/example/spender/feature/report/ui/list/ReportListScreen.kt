package com.example.spender.feature.report.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.feature.report.domain.model.Report
import com.example.spender.feature.report.ui.component.ReportSummaryCard
import com.example.spender.feature.report.ui.detail.ReportDetailScreen
import com.example.spender.ui.theme.navigation.Screen


//@Composable
//fun ReportListScreen(navHostController: NavHostController) {
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(MaterialTheme.colorScheme.background)
//    ) {
//        Text("ReportScreen", fontSize = 50.sp, fontWeight = FontWeight.Bold, color = Color.Black)
//    }
//
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(
    navHostController: NavHostController
) {
    var currentYear by remember { mutableStateOf(2025) }
    val sampleReports = listOf(
        Report(1, "2025.01", 800000, 1000000),
        Report(2, "2025.02", 1300000, 1000000),
        Report(3, "2025.03", 950000, 1000000),
        Report(4, "2025.04", 1530000, 1000000),
        Report(5, "2025.05", 1190000, 1000000),
        Report(6, "2025.06", 800000, 1000000),
        Report(7, "2025.07", 800000, 1000000),
        Report(8, "2025.08", 800000, 1000000)


    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { currentYear-- },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "이전 년도"
                            )
                        }

                        Text(
                            text = "${currentYear}년",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { if(currentYear < 2025) currentYear++ },
                            modifier = Modifier.size(36.dp).alpha(if(currentYear < 2025) 1f else 0f),
                            enabled = true
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "다음 년도"
                            )
                        }


                    }
                },
                actions = {
                    IconButton(onClick = {

                    }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "캘린더")
                    }
                }
            )
        },
        content = { padding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleReports) { report ->
                    ReportSummaryCard(
                        report = report,
                        onClick = {
                            navHostController.navigate(Screen.ReportDetail.createRoute(report.id))
                        }
                    )
                }
            }

        },
    )


}

