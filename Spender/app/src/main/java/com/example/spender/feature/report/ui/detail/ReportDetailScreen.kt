package com.example.spender.feature.report.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun ReportDetailScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            Text("${2025} 월간 지출 리포트") // TODO : 값 받아와서 해당 년도로 수정
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding()
            ){
                Text("디테일 화면!!!")
            }
        }

    )


}