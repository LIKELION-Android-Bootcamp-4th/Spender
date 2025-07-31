package com.example.spender.feature.report.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.spender.feature.report.domain.model.Report
import com.example.spender.feature.report.ui.component.ReportSummaryCard

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
    navController: NavHostController
) {
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
                    Text(
                        text = "리포트"
                    )
                }
            )
        },
        content = { padding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleReports) { report ->
                    ReportSummaryCard(
                        report = report,
                        onClick = {
                            //navController.navigate("report_detail/${report.id}")

                        }
                    )
                }
            }

        },
    )


}

